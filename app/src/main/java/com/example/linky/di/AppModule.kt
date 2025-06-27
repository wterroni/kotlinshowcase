package com.example.linky.di

import com.example.linky.core.di.networkModule
import com.example.linky.feature.shortener.di.shortenerModule
import org.koin.dsl.module

/**
 * Main application module that combines all submodules.
 * Includes network configurations, repositories, use cases, and ViewModels.
 */
val appModule = module {
    includes(
        networkModule,
        shortenerModule
    )
}
