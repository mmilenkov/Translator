package org.selostudios.translator.translation.data.history

import database.History

fun History.toHistoryItem(): HistoryItem {
    return HistoryItem(
        this.id,
        this.fromLanguage,
        this.fromText,
        this.toLanguage,
        this.toText
    )
}