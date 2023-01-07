package org.selostudios.translator.translation.domain.usecases

import org.selostudios.translator.core.domain.Language
import org.selostudios.translator.core.domain.util.Resource
import org.selostudios.translator.translation.data.history.HistoryDatasource
import org.selostudios.translator.translation.data.history.HistoryItem
import org.selostudios.translator.translation.domain.TranslationClient
import org.selostudios.translator.translation.domain.TranslationException

class TranslationUseCase(
    private val client: TranslationClient,
    private val datasource: HistoryDatasource
) {
    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = client.translate(fromLanguage,toLanguage, fromText)
            datasource.insertHistory(
                HistoryItem(null,
                    fromLanguage.code,
                    fromText,
                    toLanguage.code,
                    translatedText
                )
            )
            return Resource.Success(translatedText)
        } catch (e: TranslationException) {
            Resource.Failure(e)
        }
    }
}