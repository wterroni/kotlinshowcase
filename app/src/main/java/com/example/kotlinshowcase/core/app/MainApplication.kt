package com.example.kotlinshowcase.core.app

import android.app.Application
import android.util.Log
import com.example.kotlinshowcase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Main application class that initializes Koin for dependency injection.
 * Configures the application context and required modules for Koin.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        try {
            Log.d("MainApplication", "Initializing Koin...")
            
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(this@MainApplication)
                // Carrega os módulos do Koin
                modules(appModule)
            }
            
            Log.d("MainApplication", "Koin initialized successfully!")
        } catch (e: Exception) {
            Log.e("MainApplication", "Error initializing Koin: ${e.message}", e)
            throw e
        }
    }
}
