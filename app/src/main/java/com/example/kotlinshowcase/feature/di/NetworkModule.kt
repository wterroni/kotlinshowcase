package com.example.kotlinshowcase.feature.di

import android.util.Log
import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboService
import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboServiceImpl
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import com.example.kotlinshowcase.feature.amiibo.data.repository.AmiiboRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            engine {
                config {
                    retryOnConnectionFailure(true)
                    connectTimeout(15, TimeUnit.SECONDS)
                    readTimeout(15, TimeUnit.SECONDS)
                    writeTimeout(15, TimeUnit.SECONDS)
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("Ktor", "HTTP status: ${response.status.value}")
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }

    single<AmiiboService> {
        AmiiboServiceImpl(get())
    }

    single<AmiiboRepository> {
        AmiiboRepositoryImpl(get())
    }
}
