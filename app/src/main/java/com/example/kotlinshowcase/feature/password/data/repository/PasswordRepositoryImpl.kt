package com.example.kotlinshowcase.feature.password.data.repository

import com.example.kotlinshowcase.feature.password.data.PasswordGenerator
import com.example.kotlinshowcase.feature.password.domain.model.GeneratedPassword
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions
import com.example.kotlinshowcase.feature.password.domain.model.PasswordStrength
import com.example.kotlinshowcase.feature.password.domain.repository.PasswordRepository
import kotlin.math.min

/**
 * Implementation of [PasswordRepository] that generates passwords and calculates their strength.
 */
class PasswordRepositoryImpl(
    private val passwordGenerator: PasswordGenerator
) : PasswordRepository {

    override suspend fun generatePassword(options: PasswordOptions): GeneratedPassword {
        val password = passwordGenerator.generate(options)
        val strength = calculatePasswordStrength(password)
        return GeneratedPassword(
            password = password,
            strength = PasswordStrength.fromScore(strength),
            score = strength
        )
    }

    override suspend fun calculatePasswordStrength(password: String): Int {
        if (password.length < 4) return 0
        
        var score = 0
        
        // Length score (max 50 points)
        score += min(50, password.length * 2)
        
        // Complexity score (max 50 points)
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        
        val complexity = listOf(hasUppercase, hasLowercase, hasDigit, hasSpecial).count { it }
        score += when (complexity) {
            4 -> 50
            3 -> 30
            2 -> 15
            else -> 0
        }
        
        return min(100, score)
    }
}
