package com.example.kotlinshowcase.di

import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboService
import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboServiceImpl
import com.example.kotlinshowcase.feature.amiibo.data.repository.AmiiboRepositoryImpl
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val amiiboModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single<AmiiboService> { AmiiboServiceImpl(get()) }

    single<AmiiboRepository> { AmiiboRepositoryImpl(get()) }

    viewModel { AmiiboListViewModel(get()) }
}
