package org.selostudios.translator.translation.data.networking

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import org.selostudios.translator.Constants
import org.selostudios.translator.core.domain.Language
import org.selostudios.translator.translation.domain.TranslationError
import org.selostudios.translator.translation.domain.TranslationException
import org.selostudios.translator.translation.data.TranslationDTO
import org.selostudios.translator.translation.data.TranslationResultDTO
import org.selostudios.translator.translation.domain.TranslationClient

class TranslationClientKtor(
    private val httpClient: HttpClient
): TranslationClient {
    override suspend fun translate(
        fromLanguage: Language,
        toLanguage: Language,
        text: String
    ): String {
        val result = try {
            httpClient.post {
                url(Constants.BASE_URL + Constants.TRANSLATION_URL)
                contentType(ContentType.Application.Json)
                setBody(
                    TranslationDTO(
                        text,
                        fromLanguage.code,
                        toLanguage.code
                    )
                )
            }
        } catch (e: IOException) {
            throw TranslationException(TranslationError.SERVICE_UNAVAILABLE)
        }
        when (result.status.value) {
            in 200..299 -> Unit
            500 -> throw TranslationException(TranslationError.SERVER_ERROR)
            in 400..499 -> throw TranslationException(TranslationError.CLIENT_ERROR)
            else -> throw TranslationException(TranslationError.UNKNOWN)
        }

        return try {
            result.body<TranslationResultDTO>().translatedText
        } catch (e: Exception) {
            throw TranslationException(TranslationError.SERVER_ERROR)
        }
    }
}