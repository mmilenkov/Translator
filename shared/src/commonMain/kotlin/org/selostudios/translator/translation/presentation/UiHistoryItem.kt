package org.selostudios.translator.translation.presentation

import org.selostudios.translator.core.presentation.UiLanguage

data class UiHistoryItem(
    val id: Long,
    val fromLanguage: UiLanguage,
    val fromText: String,
    val toLanguage: UiLanguage,
    val toText: String,
)
