package com.ashehata.me_player.modules.home.presentation.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ashehata.me_player.modules.home.domain.usecase.ITracksUseCase
import com.ashehata.me_player.modules.home.presentation.mapper.toUIModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel

class TracksPagingSource(
    private val iTracksUseCase: ITracksUseCase,
    private val pageSize: Int,
) : PagingSource<Int, TrackUIModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrackUIModel> {
        return try {
            val page = params.key ?: 1
            Log.i("TracksPagingSource", "load_page: $page")

            val response =
                iTracksUseCase.execute(page = page, perPage = pageSize)
                    .map { it.toUIModel() }
            Log.i("TracksPagingSource", response.isEmpty().toString())

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            Log.e("TracksPagingSource", "Error loading page:", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TrackUIModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}