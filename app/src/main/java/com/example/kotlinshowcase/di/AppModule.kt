package com.example.kotlinshowcase.di

import com.example.kotlinshowcase.core.di.networkModule
import com.example.kotlinshowcase.feature.amiibo.di.amiiboModule
import org.koin.dsl.module

/**
 * Main application module that combines all submodules.
 * Includes network configurations, repositories, use cases, and ViewModels.
 */
val appModule = module {
    includes(
        networkModule,
        amiiboModule
    )
}
