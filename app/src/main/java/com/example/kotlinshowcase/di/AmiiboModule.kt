package com.example.kotlinshowcase.di

import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo que contém as dependências específicas da funcionalidade de Amiibo.
 * Inclui ViewModels e outras dependências específicas do módulo.
 */
val amiiboModule = module {
    // ViewModel para a lista de Amiibos
    viewModel { AmiiboListViewModel(get()) }
}
