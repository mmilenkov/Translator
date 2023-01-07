package org.selostudios.translator.translation.data.history

import kotlinx.coroutines.flow.MutableStateFlow
import org.selostudios.translator.core.domain.util.CommonFlow
import org.selostudios.translator.core.domain.util.toCommonFlow

class FakeHistoryDataSource: HistoryDatasource {
    private val _data = MutableStateFlow<List<HistoryItem>>(emptyList())

    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return _data.toCommonFlow()
    }

    override suspend fun insertHistory(item: HistoryItem) {
        _data.value += item
    }
}