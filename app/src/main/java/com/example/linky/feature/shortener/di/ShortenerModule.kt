package com.example.linky.feature.shortener.di

import com.example.linky.feature.shortener.data.remote.FallbackUrlShortenerService
import com.example.linky.feature.shortener.data.remote.MockUrlShortenerService
import com.example.linky.feature.shortener.data.remote.UrlShortenerService
import com.example.linky.feature.shortener.data.remote.UrlShortenerServiceImpl
import com.example.linky.feature.shortener.data.repository.UrlShortenerRepositoryImpl
import com.example.linky.feature.shortener.domain.repository.UrlShortenerRepository
import com.example.linky.feature.shortener.domain.usecase.GetRecentUrlsUseCase
import com.example.linky.feature.shortener.domain.usecase.ShortenUrlUseCase
import com.example.linky.feature.shortener.presentation.viewmodel.UrlShortenerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val shortenerModule = module {
    // Real API Service
    single { UrlShortenerServiceImpl(get()) }

    // Mock Service
    single { MockUrlShortenerService() }
    
    // Fallback Service (uses real API first, then falls back to mock if needed)
    single<UrlShortenerService> { FallbackUrlShortenerService(get(), get()) }
    
    // Repository
    single<UrlShortenerRepository> { UrlShortenerRepositoryImpl(get()) }
    
    // Use Cases
    factory { ShortenUrlUseCase(get()) }
    factory { GetRecentUrlsUseCase(get()) }
    
    // ViewModel
    viewModel { UrlShortenerViewModel(get(), get()) }
}
