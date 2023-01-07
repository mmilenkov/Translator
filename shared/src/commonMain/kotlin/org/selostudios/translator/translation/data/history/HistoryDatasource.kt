package org.selostudios.translator.translation.data.history

import org.selostudios.translator.core.domain.util.CommonFlow

interface HistoryDatasource {
    fun getHistory(): CommonFlow<List<HistoryItem>>
    suspend fun insertHistory(item: HistoryItem)
}