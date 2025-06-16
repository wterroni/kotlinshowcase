package com.example.kotlinshowcase.feature.password.domain.model

/**
 * Represents the options available for password generation.
 * @property length The length of the password (between 4 and 64 characters)
 * @property includeUppercase Whether to include uppercase letters
 * @property includeLowercase Whether to include lowercase letters
 * @property includeNumbers Whether to include numbers
 * @property includeSymbols Whether to include symbols
 */
data class PasswordOptions(
    val length: Int = 12,
    val includeUppercase: Boolean = true,
    val includeLowercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSymbols: Boolean = false
) {
    init {
        require(length in 4..64) { "Password length must be between 4 and 64 characters" }
        require(includeUppercase || includeLowercase || includeNumbers || includeSymbols) {
            "At least one character type must be selected"
        }
    }
}

/**
 * Represents the strength of a password.
 * @property displayName The display name of the strength level
 * @property score The numerical score (0-4) representing the strength
 * @property colorRes The color resource ID for the strength indicator
 */
enum class PasswordStrength(val displayName: String, val score: Int) {
    WEAK("Weak", 1),
    FAIR("Fair", 2),
    GOOD("Good", 3),
    STRONG("Strong", 4);

    companion object {
        fun fromScore(score: Int): PasswordStrength {
            return when {
                score >= 90 -> STRONG
                score >= 70 -> GOOD
                score >= 40 -> FAIR
                else -> WEAK
            }
        }
    }
}

/**
 * Represents a generated password with its strength information.
 * @property password The generated password
 * @property strength The strength of the password
 */
data class GeneratedPassword(
    val password: String,
    val strength: PasswordStrength,
    val score: Int
)
