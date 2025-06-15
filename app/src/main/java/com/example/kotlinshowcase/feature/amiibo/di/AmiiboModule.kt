package com.example.kotlinshowcase.feature.amiibo.di

import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboService
import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboServiceImpl
import com.example.kotlinshowcase.feature.amiibo.data.repository.AmiiboRepositoryImpl
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import org.koin.dsl.module

val amiiboModule = module {
    single<AmiiboService> { AmiiboServiceImpl(get()) }
    single<AmiiboRepository> { AmiiboRepositoryImpl(get()) }
}
