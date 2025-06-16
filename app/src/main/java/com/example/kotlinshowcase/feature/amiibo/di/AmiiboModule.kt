package com.example.kotlinshowcase.feature.amiibo.di

import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Module that contains the dependency injection definitions specific to the Amiibo module.
 * Includes ViewModels and other specific dependencies for this module.
 */
val amiiboModule = module {
    // ViewModels
    viewModel { AmiiboListViewModel(get()) }
}
