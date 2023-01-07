package org.selostudios.translator.android.translation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.selostudios.translator.translation.data.history.HistoryDatasource
import org.selostudios.translator.translation.domain.usecases.TranslationUseCase
import org.selostudios.translator.translation.presentation.TranslationEvent
import org.selostudios.translator.translation.presentation.TranslationViewModel
import javax.inject.Inject

//toFix Implement saveStateHandle if needed. Unlikely to be needed here
@HiltViewModel
class AndroidTranslationViewModel @Inject constructor(
    private val translationUseCase: TranslationUseCase,
    private val historyDatasource: HistoryDatasource,
): ViewModel() {
    private val viewModel by lazy {
        TranslationViewModel(
            translationUseCase,
            historyDatasource,
            viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslationEvent) {
        viewModel.onEvent(event)
    }
}