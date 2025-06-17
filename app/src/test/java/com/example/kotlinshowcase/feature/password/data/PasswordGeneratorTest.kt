package com.example.kotlinshowcase.feature.password.data

import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordGeneratorTest {
    
    private lateinit var passwordGenerator: PasswordGenerator
    
    @Before
    fun setup() {
        passwordGenerator = PasswordGeneratorImpl()
    }
    
    @Test
    fun `generate should return password with specified length`() {
        val length = 10
        val options = PasswordOptions(
            length = length,
            includeLowercase = true
        )
        
        val result = passwordGenerator.generate(options)
        
        assertEquals(length, result.length)
    }
    
    @Test
    fun `generate should include lowercase letters when requested`() {
        val options = PasswordOptions(
            length = 10,
            includeLowercase = true,
            includeUppercase = false,
            includeNumbers = false,
            includeSymbols = false
        )
        
        val result = passwordGenerator.generate(options)
        
        assertTrue(result.all { it.isLowerCase() })
    }
    
    @Test
    fun `generate should include uppercase letters when requested`() {
        val options = PasswordOptions(
            length = 10,
            includeLowercase = false,
            includeUppercase = true,
            includeNumbers = false,
            includeSymbols = false
        )
        
        val result = passwordGenerator.generate(options)
        
        assertTrue(result.all { it.isUpperCase() })
    }
    
    @Test
    fun `generate should include numbers when requested`() {
        val options = PasswordOptions(
            length = 10,
            includeLowercase = false,
            includeUppercase = false,
            includeNumbers = true,
            includeSymbols = false
        )
        
        val result = passwordGenerator.generate(options)
        
        assertTrue(result.all { it.isDigit() })
    }
    
    @Test
    fun `generate should include symbols when requested`() {
        val symbols = "!@#\$%^&*()_+-=[]{}|;:,.<>?\"/"
        val options = PasswordOptions(
            length = 20,
            includeLowercase = false,
            includeUppercase = false,
            includeNumbers = false,
            includeSymbols = true
        )
        
        val result = passwordGenerator.generate(options)
        
        assertTrue(result.any { it in symbols })
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `generate should throw exception when no character type is selected`() {
        val options = PasswordOptions(
            length = 10,
            includeLowercase = false,
            includeUppercase = false,
            includeNumbers = false,
            includeSymbols = false
        )
        
        passwordGenerator.generate(options)
    }
    
    @Test
    fun `generate should include all requested character types`() {
        val options = PasswordOptions(
            length = 64,
            includeLowercase = true,
            includeUppercase = true,
            includeNumbers = true,
            includeSymbols = true
        )
        
        val result = passwordGenerator.generate(options)
        
        assertTrue(result.any { it.isLowerCase() })
        assertTrue(result.any { it.isUpperCase() })
        assertTrue(result.any { it.isDigit() })
        assertTrue(result.any { !it.isLetterOrDigit() })
    }
}
