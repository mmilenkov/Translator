package org.selostudios.translator.core.presentation

import org.selostudios.translator.core.domain.Language

actual class UiLanguage(
    actual val language: Language,
    val imageName: String
) {
    actual companion object {
        actual fun fromCode(code: String): UiLanguage =
            languages.find { it.language.code == code } ?:
            throw IllegalArgumentException("Invalid or unsupported language code")


        actual val languages: List<UiLanguage>
            get() = Language.values().map { language ->
                UiLanguage(
                    language,
                    language.languageName.lowercase()
                )
            }
    }
}