package com.example.kotlinshowcase.feature.textutils.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CapitalizeWordsUseCaseTest {
    
    private lateinit var useCase: CapitalizeWordsUseCase
    
    @Before
    fun setup() {
        useCase = CapitalizeWordsUseCase()
    }
    
    @Test
    fun `invoke should return empty string when input is empty`() {
        val result = useCase("")
        assertEquals("", result)
    }
    
    @Test
    fun `invoke should capitalize single word`() {
        val result = useCase("hello")
        assertEquals("Hello", result)
    }
    
    @Test
    fun `invoke should capitalize each word in a sentence`() {
        val result = useCase("hello world")
        assertEquals("Hello World", result)
    }
    
    @Test
    fun `invoke should handle multiple spaces between words`() {
        val result = useCase("hello   world")
        assertEquals("Hello   World", result)
    }
    
    @Test
    fun `invoke should handle leading and trailing spaces`() {
        val result = useCase("  hello world  ")
        assertEquals("  Hello World  ", result)
    }
    
    @Test
    fun `invoke should handle mixed case input`() {
        val result = useCase("hElLo WoRlD")
        assertEquals("Hello World", result)
    }
    
    @Test
    fun `invoke should handle special characters`() {
        val result = useCase("hello, world! how are you?")
        assertEquals("Hello, World! How Are You?", result)
    }
    
    @Test
    fun `invoke should handle single character words`() {
        val result = useCase("a b c d e")
        assertEquals("A B C D E", result)
    }
    
    @Test
    fun `invoke should handle non-letter characters`() {
        val result = useCase("123 abc 456 def")
        assertEquals("123 Abc 456 Def", result)
    }
    
    @Test
    fun `invoke should handle unicode characters`() {
        val result = useCase("olá mundo ção")
        // A função capitaliza o primeiro caractere de cada palavra, incluindo caracteres acentuados
        assertEquals("Olá Mundo Ção", result)
    }
    
    @Test
    fun `invoke should handle empty words`() {
        val result = useCase("  a   b   c   ")
        assertEquals("  A   B   C   ", result)
    }
}
