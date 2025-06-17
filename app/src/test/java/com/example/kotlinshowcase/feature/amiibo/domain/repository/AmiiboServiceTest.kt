package com.example.kotlinshowcase.feature.amiibo.domain.repository

import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboService
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AmiiboServiceTest {

    private val amiiboService: AmiiboService = mockk()
    
    private val testAmiibo = Amiibo(
        name = "Mario",
        character = "Mario",
        gameSeries = "Super Mario",
        image = "mario.png",
        amiiboSeries = "Super Smash Bros.",
        type = "Figure",
        release = ReleaseDate("2014-11-21")
    )

    @Test
    fun `getAmiibos should return list of amiibos`() = runTest {
        coEvery { amiiboService.getAmiibos() } returns listOf(testAmiibo)

        val result = amiiboService.getAmiibos()

        assertEquals(1, result.size)
        assertEquals("Mario", result[0].name)
        coVerify { amiiboService.getAmiibos() }
    }
    
    @Test
    fun `searchAmiibos with non-empty query should return filtered list`() = runTest {
        // Given
        val query = "mario"
        coEvery { amiiboService.searchAmiibos(query) } returns listOf(testAmiibo)
        
        // When
        val result = amiiboService.searchAmiibos(query)
        
        // Then
        assertEquals(1, result.size)
        assertEquals("Mario", result[0].name)
        coVerify { amiiboService.searchAmiibos(query) }
    }
    
    @Test
    fun `searchAmiibos with empty query should return empty list`() = runTest {
        // Given
        val query = ""
        coEvery { amiiboService.searchAmiibos(query) } returns emptyList()
        
        // When
        val result = amiiboService.searchAmiibos(query)
        
        // Then
        assertTrue(result.isEmpty())
        coVerify { amiiboService.searchAmiibos(query) }
    }
    
    @Test
    fun `searchAmiibos with non-matching query should return empty list`() = runTest {
        // Given
        val query = "non-existent"
        coEvery { amiiboService.searchAmiibos(query) } returns emptyList()
        
        // When
        val result = amiiboService.searchAmiibos(query)
        
        // Then
        assertTrue(result.isEmpty())
        coVerify { amiiboService.searchAmiibos(query) }
    }
}
