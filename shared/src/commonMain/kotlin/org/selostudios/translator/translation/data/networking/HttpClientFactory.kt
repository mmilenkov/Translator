package org.selostudios.translator.translation.data.networking

import io.ktor.client.*

expect class HttpClientFactory {
    fun create(): HttpClient
}