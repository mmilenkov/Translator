package org.selostudios.translator.translation.data.history

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import database.TranslationQueries
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import org.selostudios.translator.core.domain.util.CommonFlow
import org.selostudios.translator.core.domain.util.toCommonFlow
import org.selostudios.translator.db.TranslationDB

class SQLDelightHistoryDatasource(db: TranslationDB): HistoryDatasource {
    private val queries: TranslationQueries = db.translationQueries

    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return queries.getHistory()
            .asFlow()
            .mapToList()
            .map { history ->
                history.map {
                    it.toHistoryItem()
                }
            }.toCommonFlow()
    }

    override suspend fun insertHistory(item: HistoryItem) {
        queries.insertHistory(
            item.id,
            fromLanguage = item.fromLanguage,
            fromText = item.fromText,
            toLanguage = item.toLanguage,
            toText = item.toText,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }
}