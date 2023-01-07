package org.selostudios.translator.core.presentation

import org.selostudios.translator.core.domain.Language

expect class UiLanguage {
    val language: Language
    companion object {
        fun fromCode(code: String) : UiLanguage
        val languages: List<UiLanguage>
    }
}