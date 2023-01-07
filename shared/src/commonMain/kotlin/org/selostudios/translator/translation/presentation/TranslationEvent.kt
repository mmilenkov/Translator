package org.selostudios.translator.translation.presentation

import org.selostudios.translator.core.presentation.UiLanguage

sealed class TranslationEvent {
    data class ChooseFromLanguage(val language: UiLanguage): TranslationEvent()
    data class ChooseToLanguage(val language: UiLanguage): TranslationEvent()
    object StopChoosingLanguage: TranslationEvent()
    object SwapLanguages: TranslationEvent()
    data class ChangeTranslationText(val text: String): TranslationEvent()
    object Translate: TranslationEvent()
    object OpenFromLanguageDropdown: TranslationEvent()
    object OpenToLanguageDropdown: TranslationEvent()
    object CloseTranslation: TranslationEvent()
    data class SelectHistoryItem(val item: UiHistoryItem): TranslationEvent()
    object EditTranslation: TranslationEvent()
    object RecordAudio: TranslationEvent()
    data class SubmitVoiceResult(val result: String?): TranslationEvent()
    object OnErrorSeen: TranslationEvent()
}
