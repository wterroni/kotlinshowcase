package com.example.kotlinshowcase.feature.password.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinshowcase.feature.password.domain.model.GeneratedPassword
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions
import com.example.kotlinshowcase.feature.password.domain.usecase.GeneratePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * ViewModel for the Password Generator screen.
 */
class PasswordGeneratorViewModel(
    private val generatePassword: GeneratePasswordUseCase
) : ViewModel() {

    companion object {
        fun module() = module {
            viewModel { PasswordGeneratorViewModel(get()) }
        }
    }
    
    private val _uiState = MutableStateFlow<PasswordGeneratorUiState>(PasswordGeneratorUiState.Loading)
    val uiState: StateFlow<PasswordGeneratorUiState> = _uiState.asStateFlow()
    
    var passwordOptions by mutableStateOf(
        PasswordOptions(
            length = 12,
            includeUppercase = true,
            includeLowercase = true,
            includeNumbers = true,
            includeSymbols = false
        )
    )
        private set
    
    init {
        generateNewPassword()
    }
    
    /**
     * Updates the password options and generates a new password.
     */
    fun updateOptions(options: PasswordOptions) {
        passwordOptions = options
        generateNewPassword()
    }
    
    /**
     * Generates a new password with the current options.
     */
    fun generateNewPassword() {
        viewModelScope.launch {
            _uiState.value = PasswordGeneratorUiState.Loading
            try {
                val generatedPassword = generatePassword(passwordOptions)
                _uiState.value = PasswordGeneratorUiState.Success(generatedPassword)
            } catch (e: Exception) {
                _uiState.value = PasswordGeneratorUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

/**
 * Represents the different states of the password generator UI.
 */
sealed class PasswordGeneratorUiState {
    data object Loading : PasswordGeneratorUiState()
    data class Success(val password: GeneratedPassword) : PasswordGeneratorUiState()
    data class Error(val message: String) : PasswordGeneratorUiState()
}
