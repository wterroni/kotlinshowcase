package com.example.kotlinshowcase.core.app

import android.app.Application
import android.util.Log
import com.example.kotlinshowcase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Classe principal da aplicação que inicializa o Koin para injeção de dependência.
 * Configura o contexto da aplicação e os módulos necessários para o Koin.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        try {
            Log.d("MainApplication", "Inicializando Koin...")
            
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(this@MainApplication)
                // Carrega os módulos do Koin
                modules(appModule)
            }
            
            Log.d("MainApplication", "Koin inicializado com sucesso!")
        } catch (e: Exception) {
            Log.e("MainApplication", "Erro ao inicializar Koin: ${e.message}", e)
            throw e
        }
    }
}
