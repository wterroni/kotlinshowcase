package com.example.kotlinshowcase.feature.password.data.repository

import com.example.kotlinshowcase.feature.password.data.PasswordGenerator
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions
import com.example.kotlinshowcase.feature.password.domain.model.PasswordStrength
import io.mockk.every
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordRepositoryImplTest {
    
    private lateinit var repository: PasswordRepositoryImpl
    private val mockPasswordGenerator: PasswordGenerator = mockk()
    
    @Before
    fun setup() {
        repository = PasswordRepositoryImpl(mockPasswordGenerator)
    }
    
    @Test
    fun `generatePassword should return generated password with strength`() = runTest {
        // Given
        val password = "Test123!"
        val options = PasswordOptions(
            length = 8,
            includeLowercase = true,
            includeUppercase = true,
            includeNumbers = true,
            includeSymbols = true
        )
        
        every { mockPasswordGenerator.generate(options) } returns password

        val result = repository.generatePassword(options)

        assertEquals(password, result.password)
        assertTrue(result.score in 0..100)
        assertTrue(result.strength in PasswordStrength.values().toList())
        
        coVerify { mockPasswordGenerator.generate(options) }
    }
    
    @Test
    fun `calculatePasswordStrength should return 0 for empty password`() = runTest {
        val password = ""

        val result = repository.calculatePasswordStrength(password)

        assertEquals(0, result)
    }
    
    @Test
    fun `calculatePasswordStrength should return higher score for longer passwords`() = runTest {
        val shortPassword = "abc"
        val longPassword = "abcdefghijklmnopqrstuvwxyz"

        val shortScore = repository.calculatePasswordStrength(shortPassword)
        val longScore = repository.calculatePasswordStrength(longPassword)

        assertTrue(longScore > shortScore)
    }
    
    @Test
    fun `calculatePasswordStrength should return higher score for more complex passwords`() = runTest {
        val simplePassword = "password"
        val complexPassword = "P@ssw0rd!"

        val simpleScore = repository.calculatePasswordStrength(simplePassword)
        val complexScore = repository.calculatePasswordStrength(complexPassword)

        assertTrue(complexScore > simpleScore)
    }
    
    @Test
    fun `calculatePasswordStrength should return high score for very strong password`() = runTest {
        val strongPassword = "V3ry\$tr0ngP@ssw0rd!123"

        val score = repository.calculatePasswordStrength(strongPassword)

        assertTrue("A pontuação $score deve ser considerada forte (>= 90)", score >= 90)
    }
    
    @Test
    fun `calculatePasswordStrength should return higher score for passwords with more character types`() = runTest {
        val lowercaseOnly = "lowercase"
        val withUppercase = "Lowercase"
        val withNumber = "Lowercas3"
        val withSymbol = "Lowercas3!"

        val score1 = repository.calculatePasswordStrength(lowercaseOnly)
        val score2 = repository.calculatePasswordStrength(withUppercase)
        val score3 = repository.calculatePasswordStrength(withNumber)
        val score4 = repository.calculatePasswordStrength(withSymbol)

        assertTrue(score2 > score1)
        assertTrue(score3 > score2)
        assertTrue(score4 > score3)
    }
}
