package org.selostudios.translator.translation.domain

import org.selostudios.translator.core.domain.Language

interface TranslationClient {
    suspend fun translate(
        fromLanguage: Language,
        toLanguage: Language,
        text: String
    ) : String
}