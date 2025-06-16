package com.example.kotlinshowcase.feature.textutils.di

import com.example.kotlinshowcase.feature.textutils.domain.usecase.CapitalizeWordsUseCase
import org.koin.dsl.module

/**
 * Koin module for the Text Utils feature.
 */
val textUtilsModule = module {
    // Domain layer
    factory {
        CapitalizeWordsUseCase() 
    }
}
