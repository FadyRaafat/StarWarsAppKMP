package com.fady.starwars.platform

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

actual fun createHttpClient(): HttpClient {
    return HttpClient(Android) {
        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }
    }
}