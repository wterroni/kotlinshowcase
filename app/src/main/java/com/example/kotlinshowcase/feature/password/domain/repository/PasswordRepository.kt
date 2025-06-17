package com.example.kotlinshowcase.feature.password.domain.repository

import com.example.kotlinshowcase.feature.password.domain.model.GeneratedPassword
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions

/**
 * Repository interface for password generation operations.
 */
interface PasswordRepository {
    /**
     * Generates a password based on the provided options.
     * @param options The options to use for password generation
     * @return A [GeneratedPassword] containing the password and its strength information
     */
    suspend fun generatePassword(options: PasswordOptions): GeneratedPassword
    
    /**
     * Calculates the strength score of a password.
     * @param password The password to evaluate
     * @return A score between 0 and 100 representing the password strength
     */
    suspend fun calculatePasswordStrength(password: String): Int
}
