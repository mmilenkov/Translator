package org.selostudios.translator.translation.presentation

import org.selostudios.translator.core.presentation.UiLanguage
import org.selostudios.translator.translation.domain.TranslationError

data class TranslationState(
    val fromLanguage: UiLanguage = UiLanguage.fromCode("en"),
    val fromText: String = "",
    val toLanguage: UiLanguage = UiLanguage.fromCode("en"),
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val error: TranslationError? = null,
    val history: List<UiHistoryItem> = emptyList()
)
