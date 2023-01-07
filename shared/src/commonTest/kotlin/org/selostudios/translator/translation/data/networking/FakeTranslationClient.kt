package org.selostudios.translator.translation.data.networking

import org.selostudios.translator.core.domain.Language
import org.selostudios.translator.translation.domain.TranslationClient

class FakeTranslationClient: TranslationClient {
    var translatedText = "Sample translated text"

    override suspend fun translate(
        fromLanguage: Language,
        toLanguage: Language,
        text: String
    ): String {
        return translatedText
    }
}