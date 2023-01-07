package org.selostudios.translator.translation.domain

enum class TranslationError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN
}

class TranslationException(val error: TranslationError) : Exception("Error occurred during translation: $error")