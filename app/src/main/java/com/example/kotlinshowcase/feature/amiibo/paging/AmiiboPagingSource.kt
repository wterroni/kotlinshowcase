package com.example.kotlinshowcase.feature.amiibo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import java.io.IOException
import kotlin.math.min

class AmiiboPagingSource(
    private val repository: AmiiboRepository
) : PagingSource<Int, Amiibo>() {

    override fun getRefreshKey(state: PagingState<Int, Amiibo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Amiibo> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            
            // Busca todos os amiibos uma única vez
            val allAmiibos = repository.getAmiibos()
            
            // Se a lista estiver vazia, retorna página vazia
            if (allAmiibos.isEmpty()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = null
                )
            }
            
            val fromIndex = page * pageSize
            if (fromIndex >= allAmiibos.size) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = null
                )
            }
            
            val toIndex = min(fromIndex + pageSize, allAmiibos.size)
            val pageAmiibos = allAmiibos.subList(fromIndex, toIndex)
            
            LoadResult.Page(
                data = pageAmiibos,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (toIndex < allAmiibos.size) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
