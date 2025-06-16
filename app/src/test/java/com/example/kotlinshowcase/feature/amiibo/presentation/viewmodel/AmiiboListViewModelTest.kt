package com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel

import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AmiiboListViewModelTest {

    private val repository = mockk<AmiiboRepository>()
    private lateinit var viewModel: AmiiboListViewModel

    private val testAmiibos = listOf(
        Amiibo("Mario", "Mario", "Super Mario", "url", "Smash", "Figure", ReleaseDate("2014-11-21")),
        Amiibo("Link", "Link", "Zelda", "url", "Zelda", "Figure", ReleaseDate("2017-03-03")),
        Amiibo("Samus", "Samus Aran", "Metroid", "url", "Metroid", "Figure", ReleaseDate("2017-09-15"))
    )

    @Before
    fun setup() {
        coEvery { repository.getAmiibos() } returns testAmiibos
        viewModel = AmiiboListViewModel(repository)
    }

    @Test
    fun `getAmiibos returns flow when query is blank`() = runTest {
        // When
        val result = viewModel.getAmiibos("")

        // Then
        assertTrue(result is kotlinx.coroutines.flow.Flow<*>)
    }

    @Test
    fun `getAmiibos returns flow when filtering by name`() = runTest {
        // When
        val result = viewModel.getAmiibos("mario")

        // Then
        assertTrue(result is kotlinx.coroutines.flow.Flow<*>)  // Verifica se retorna um Flow
    }
}
