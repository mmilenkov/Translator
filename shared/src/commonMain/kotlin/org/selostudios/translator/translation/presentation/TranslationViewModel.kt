package org.selostudios.translator.translation.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.selostudios.translator.core.domain.util.Resource
import org.selostudios.translator.core.domain.util.toCommonStateFlow
import org.selostudios.translator.core.presentation.UiLanguage
import org.selostudios.translator.translation.data.history.HistoryDatasource
import org.selostudios.translator.translation.domain.TranslationException
import org.selostudios.translator.translation.domain.usecases.TranslationUseCase

class TranslationViewModel(
    private val translationUseCase: TranslationUseCase,
    private val historyDatasource: HistoryDatasource,
    private val coroutineScope: CoroutineScope?
) {
    //iOS uses the main dispatcher as such if we don't pass anything default to it
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(TranslationState())
    val state = combine(_state, historyDatasource.getHistory()) { state, history ->
        if (state.history != history) {
            state.copy(
                history = history.mapNotNull { item ->
                    UiHistoryItem(
                        item.id ?: return@mapNotNull null,
                        UiLanguage.fromCode(item.fromLanguage),
                        item.fromText,
                        UiLanguage.fromCode(item.toLanguage),
                        item.toText
                    )
                }
            )
        } else {
            state
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TranslationState()
    ).toCommonStateFlow()

    private var translationJob: Job? = null

    fun onEvent(event: TranslationEvent) {
        when (event) {
            is TranslationEvent.ChangeTranslationText -> {
                _state.update { state ->
                    state.copy(
                        fromText = event.text
                    )
                }
            }
            is TranslationEvent.ChooseFromLanguage -> {
                _state.update { state ->
                    state.copy(
                        isChoosingFromLanguage = false,
                        fromLanguage = event.language
                    )
                }
            }
            is TranslationEvent.ChooseToLanguage -> {
                translate(
                    _state.updateAndGet { state ->
                    state.copy(
                        isChoosingToLanguage = false,
                        toLanguage = event.language
                    )
                })
            }
            TranslationEvent.CloseTranslation -> {
                _state.update { state ->
                    state.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }
            TranslationEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update { state ->
                        state.copy(
                            toText = null,
                            isTranslating = false
                        )
                    }
                }
            }
            TranslationEvent.OnErrorSeen -> {
                _state.update { state ->
                    state.copy(
                        error = null
                    )
                }
            }
            TranslationEvent.OpenFromLanguageDropdown -> {
                _state.update { state ->
                    state.copy(
                        isChoosingFromLanguage = true
                    )
                }
            }
            TranslationEvent.OpenToLanguageDropdown -> {
                _state.update { state ->
                    state.copy(
                        isChoosingToLanguage = true
                    )
                }
            }
            is TranslationEvent.SelectHistoryItem -> {
                translationJob?.cancel()
                _state.update { state ->
                    state.copy(
                        fromText = event.item.fromText,
                        toText = event.item.toText,
                        isTranslating = false,
                        fromLanguage = event.item.fromLanguage,
                        toLanguage = event.item.toLanguage
                    )
                }
            }
            TranslationEvent.StopChoosingLanguage -> {
                _state.update { state ->
                    state.copy(
                        isChoosingFromLanguage = false,
                        isChoosingToLanguage = false
                    )
                }
            }
            is TranslationEvent.SubmitVoiceResult -> {
                _state.update { state ->
                    state.copy(
                        fromText = event.result ?: state.fromText,
                        isTranslating = if (event.result != null) true else state.isTranslating,
                        toText = if (event.result != null) null else state.toText
                    )
                }
            }
            TranslationEvent.SwapLanguages -> {
                _state.update { state ->
                    state.copy(
                        fromLanguage = state.toLanguage,
                        fromText = state.toText ?: "",
                        toLanguage = state.fromLanguage,
                        toText = if (state.toText != null) state.fromText else null
                    )
                }
            }
            TranslationEvent.Translate -> {
                translate(state.value)
            }
            else -> Unit
        }
    }

    private fun translate(state: TranslationState) {
        if (state.isTranslating || state.fromText.isBlank()) {
            return
        }
        translationJob = viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isTranslating = true
                )
            }
            val result = translationUseCase.execute(
                state.fromLanguage.language,
                state.fromText,
                state.toLanguage.language
            )
            when (result) {
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(
                            isTranslating = false,
                            toText = result.data
                        )
                    }
                }
                is Resource.Failure -> {
                    _state.update { state ->
                        state.copy(
                            isTranslating = false,
                            error = (result.throwable as? TranslationException)?.error
                        )
                    }
                }
            }
        }

    }
}