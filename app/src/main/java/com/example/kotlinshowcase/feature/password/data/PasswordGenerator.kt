package com.example.kotlinshowcase.feature.password.data

import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions

/**
 * Interface for generating passwords based on specified options.
 */
interface PasswordGenerator {
    /**
     * Generates a password based on the provided options.
     * @param options The options to use for password generation
     * @return The generated password as a String
     */
    fun generate(options: PasswordOptions): String
}

/**
 * Default implementation of [PasswordGenerator].
 */
class PasswordGeneratorImpl : PasswordGenerator {
    private val lowercaseChars = ('a'..'z').toList()
    private val uppercaseChars = ('A'..'Z').toList()
    private val numberChars = ('0'..'9').toList()
    private val symbolChars = "!@#\$%^&*()_+-=[]{}|;:,.<>?\"/".toList()

    override fun generate(options: PasswordOptions): String {
        val charPool = mutableListOf<Char>()
        
        if (options.includeLowercase) charPool.addAll(lowercaseChars)
        if (options.includeUppercase) charPool.addAll(uppercaseChars)
        if (options.includeNumbers) charPool.addAll(numberChars)
        if (options.includeSymbols) charPool.addAll(symbolChars)
        
        require(charPool.isNotEmpty()) { "At least one character type must be selected" }
        
        return (1..options.length)
            .map { charPool.random() }
            .joinToString("")
    }
}
