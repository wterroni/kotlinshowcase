package com.example.kotlinshowcase.feature.textutils.domain.usecase

/**
 * Use case that capitalizes the first letter of each word in a string.
 * 
 * This function processes each character individually and capitalizes the first letter of each word.
 * A word is considered any sequence of non-whitespace characters separated by whitespace.
 * 
 * @param input The input string to be processed
 * @return A new string with the first letter of each word capitalized
 * 
 * Example:
 * ```

 * ```
 */
class CapitalizeWordsUseCase {
    operator fun invoke(input: String): String {
        if (input.isEmpty()) return ""
        
        val result = StringBuilder()
        var capitalizeNext = true
        
        for (char in input) {
            when {
                char.isWhitespace() -> {
                    result.append(char)
                    capitalizeNext = true
                }
                capitalizeNext -> {
                    result.append(char.uppercaseChar())
                    capitalizeNext = false
                }
                else -> {
                    result.append(char.lowercaseChar())
                }
            }
        }
        
        return result.toString()
    }
}
