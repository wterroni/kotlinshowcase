package com.example.kotlinshowcase.feature.amiibo.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AmiiboPagingSourceTest {

    private lateinit var pagingSource: AmiiboPagingSource
    private val repository: AmiiboRepository = mockk()
    
    private val testAmiibos = List(50) { index ->
        Amiibo(
            "Amiibo $index",
            "Character $index",
            "Game Series $index",
            "url$index",
            "Amiibo Series $index",
            "Figure",
            ReleaseDate("2020-01-01")
        )
    }

    @Before
    fun setup() {
        coEvery { repository.getAmiibos() } returns testAmiibos
        pagingSource = AmiiboPagingSource(repository)
    }

    @Test
    fun `load returns first page of items`() = runTest {
        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 10,
            placeholdersEnabled = false
        )

        val result = pagingSource.load(loadParams)
        
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(10, page.data.size)
        assertEquals(testAmiibos.take(10), page.data)
        assertNull(page.prevKey)
        assertEquals(1, (result as? PagingSource.LoadResult.Page)?.nextKey)
    }

    @Test
    fun `load returns second page of items`() = runTest {
        val loadParams = PagingSource.LoadParams.Append(
            key = 1,
            loadSize = 10,
            placeholdersEnabled = false
        )

        val result = pagingSource.load(loadParams)
        
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(10, page.data.size)
        assertEquals(testAmiibos.slice(10..19), page.data)
        assertEquals(0, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load returns empty list when index out of bounds`() = runTest {
        val loadParams = PagingSource.LoadParams.Append(
            key = 100, // Fora dos limites
            loadSize = 10,
            placeholdersEnabled = false
        )

        val result = pagingSource.load(loadParams)
        
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data.isEmpty())
    }

    @Test
    fun `refreshKey returns null when list is empty`() = runTest {
        coEvery { repository.getAmiibos() } returns emptyList()
        
        val state = PagingState<Int, Amiibo>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )
        
        assertNull(pagingSource.getRefreshKey(state))
    }

    @Test
    fun `refreshKey returns nextKey minus one when anchor position is not null`() = runTest {
        val state = PagingState<Int, Amiibo>(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = testAmiibos.take(10),
                    prevKey = 0,
                    nextKey = 2 // Alterado para 2 para o teste fazer sentido
                )
            ),
            anchorPosition = 5,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        assertEquals(1, pagingSource.getRefreshKey(state))
    }
}
