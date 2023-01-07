package org.selostudios.translator.translation.data

import kotlinx.serialization.SerialName
import org.selostudios.translator.Constants

@kotlinx.serialization.Serializable
data class TranslationDTO(
    @SerialName("q")
    val text: String,
    @SerialName("source")
    val fromLanguageCode: String,
    @SerialName("target")
    val toLanguageCode: String,
    val format: String = "text",
    val api_key: String = Constants.API_KEY
)
