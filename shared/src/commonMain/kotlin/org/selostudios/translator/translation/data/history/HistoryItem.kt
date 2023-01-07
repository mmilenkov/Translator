package org.selostudios.translator.translation.data.history

data class HistoryItem(
    val id: Long?,
    val fromLanguage: String,
    val fromText: String,
    val toLanguage: String,
    val toText: String,
)
