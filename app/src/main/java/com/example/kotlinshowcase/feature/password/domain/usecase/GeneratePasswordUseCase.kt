package com.example.kotlinshowcase.feature.password.domain.usecase

import com.example.kotlinshowcase.feature.password.domain.model.GeneratedPassword
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions
import com.example.kotlinshowcase.feature.password.domain.repository.PasswordRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Use case for generating passwords.
 */
class GeneratePasswordUseCase : KoinComponent {
    private val repository: PasswordRepository by inject()
    
    /**
     * Generates a password with the specified options.
     * @param options The options to use for password generation
     * @return A [GeneratedPassword] containing the password and its strength information
     */
    suspend operator fun invoke(options: PasswordOptions): GeneratedPassword {
        return repository.generatePassword(options)
    }
}
