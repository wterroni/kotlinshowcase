package com.example.kotlinshowcase.core.app

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import com.example.kotlinshowcase.di.appModule
import com.example.kotlinshowcase.feature.textutils.di.textUtilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinApplication
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

                modules(
                    appModule,
                    textUtilsModule
                )
            }
            
            Log.d("MainApplication", "Koin initialized successfully!")
        } catch (e: Exception) {
            Log.e("MainApplication", "Error initializing Koin: ${e.message}", e)
            throw e
        }
    }
}

/**
 * Koin application wrapper for Compose
 */
@Composable
fun KoinApp(
    content: @Composable () -> Unit
) {
    KoinApplication(application = {
        modules(appModule)
    }) {
        content()
    }
}
