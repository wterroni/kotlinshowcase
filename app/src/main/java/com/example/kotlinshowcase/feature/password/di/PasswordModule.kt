package com.example.kotlinshowcase.feature.password.di

import com.example.kotlinshowcase.feature.password.data.PasswordGeneratorImpl
import com.example.kotlinshowcase.feature.password.data.repository.PasswordRepositoryImpl
import com.example.kotlinshowcase.feature.password.domain.repository.PasswordRepository
import com.example.kotlinshowcase.feature.password.domain.usecase.GeneratePasswordUseCase
import com.example.kotlinshowcase.feature.password.presentation.viewmodel.PasswordGeneratorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for the Password feature.
 */
val passwordModule = module {

    single<PasswordRepository> { PasswordRepositoryImpl(PasswordGeneratorImpl()) }
    

    factory { GeneratePasswordUseCase() }
    

    viewModel { PasswordGeneratorViewModel(get()) }
}
