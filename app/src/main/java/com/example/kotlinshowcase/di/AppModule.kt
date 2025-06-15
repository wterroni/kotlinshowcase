package com.example.kotlinshowcase.di

import com.example.kotlinshowcase.feature.amiibo.di.amiiboModule
import com.example.kotlinshowcase.feature.di.networkModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        amiiboModule
    )
}
