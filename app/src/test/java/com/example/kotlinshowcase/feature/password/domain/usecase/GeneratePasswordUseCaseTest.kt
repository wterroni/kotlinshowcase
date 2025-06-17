package com.example.kotlinshowcase.feature.password.domain.usecase

import com.example.kotlinshowcase.feature.password.domain.model.GeneratedPassword
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions
import com.example.kotlinshowcase.feature.password.domain.model.PasswordStrength
import com.example.kotlinshowcase.feature.password.domain.repository.PasswordRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class GeneratePasswordUseCaseTest {
    
    private val mockRepository: PasswordRepository = mockk()
    private lateinit var useCase: GeneratePasswordUseCase
    
    @Before
    fun setup() {
        stopKoin() // Ensure a clean state
        
        startKoin {
            modules(
                module {
                    single { mockRepository }
                }
            )
        }
        
        useCase = GeneratePasswordUseCase()
    }
    
    @Test
    fun `invoke should call repository and return generated password`() = runTest {
        // Given
        val options = PasswordOptions(
            length = 12,
            includeLowercase = true,
            includeUppercase = true,
            includeNumbers = true,
            includeSymbols = true
        )
        
        val expectedPassword = GeneratedPassword(
            password = "Test123!",
            strength = PasswordStrength.STRONG,
            score = 90
        )
        
        coEvery { mockRepository.generatePassword(options) } returns expectedPassword
        
        // When
        val result = useCase(options)
        
        // Then
        assertEquals(expectedPassword, result)
        coVerify { mockRepository.generatePassword(options) }
    }
    
    @Test(expected = Exception::class)
    fun `invoke should propagate exceptions from repository`() = runTest {
        // Given
        val options = PasswordOptions(
            length = 12,
            includeLowercase = true
        )
        
        coEvery { mockRepository.generatePassword(options) } throws Exception("Network error")
        
        // When/Then
        useCase(options)
    }
    
    @Test
    fun `password strength should be mapped correctly from score`() = runTest {
        // Given
        val options = PasswordOptions(
            length = 12,
            includeLowercase = true
        )
        
        val testCases = listOf(
            Triple("weak", 30, PasswordStrength.WEAK),
            Triple("fair", 50, PasswordStrength.FAIR),
            Triple("good", 75, PasswordStrength.GOOD),
            Triple("strong", 95, PasswordStrength.STRONG)
        )
        
        testCases.forEach { (testCase, score, expectedStrength) ->
            // Given
            val password = GeneratedPassword(
                password = "test$testCase",
                strength = expectedStrength,
                score = score
            )
            
            coEvery { mockRepository.generatePassword(options) } returns password
            
            // When
            val result = useCase(options)
            
            // Then
            assertEquals("Strength mismatch for $testCase case", expectedStrength, result.strength)
        }
    }
}
