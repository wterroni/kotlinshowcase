package com.example.kotlinshowcase.feature.amiibo.data.repository

import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboService
import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AmiiboRepositoryImplTest {

    private lateinit var repository: AmiiboRepository
    private val service: AmiiboService = mockk()
    
    private val testAmiibos = listOf(
        Amiibo("Mario", "Mario", "Super Mario", "url1", "Smash", "Figure", ReleaseDate("2014-11-21")),
        Amiibo("Link", "Link", "Zelda", "url2", "Zelda", "Figure", ReleaseDate("2017-03-03")),
        Amiibo("Samus", "Samus Aran", "Metroid", "url3", "Metroid", "Figure", ReleaseDate("2017-09-15"))
    )

    @Before
    fun setup() {
        repository = AmiiboRepositoryImpl(service)
    }

    @Test
    fun `getAmiibos should return list of amiibos from service`() = runTest {
        coEvery { service.getAmiibos() } returns testAmiibos

        val result = repository.getAmiibos()

        assertEquals(testAmiibos.size, result.size)
        assertEquals(testAmiibos, result)
    }
    
    @Test
    fun `searchAmiibos with empty query should return empty list`() = runTest {
        val query = ""

        val result = repository.searchAmiibos(query)

        assertTrue(result.isEmpty())
    }
    
    @Test
    fun `searchAmiibos with non-empty query should return filtered results from service`() = runTest {
        val query = "mario"
        coEvery { service.searchAmiibos(query) } returns testAmiibos.filter { it.name.contains(query, ignoreCase = true) }

        val result = repository.searchAmiibos(query)

        assertEquals(1, result.size)
        assertEquals("Mario", result[0].name)
    }
    
    @Test
    fun `searchAmiibos with non-matching query should return empty list`() = runTest {
        val query = "non-existent-character"
        coEvery { service.searchAmiibos(query) } returns emptyList()

        val result = repository.searchAmiibos(query)

        assertTrue(result.isEmpty())
    }
}
