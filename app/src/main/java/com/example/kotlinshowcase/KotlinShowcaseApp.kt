package com.example.kotlinshowcase

import android.app.Application
import com.example.kotlinshowcase.di.amiiboModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KotlinShowcaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@KotlinShowcaseApp)
            modules(amiiboModule)
        }
    }
}
