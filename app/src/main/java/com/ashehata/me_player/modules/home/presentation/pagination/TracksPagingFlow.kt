package com.ashehata.me_player.modules.home.presentation.pagination

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ashehata.me_player.modules.home.domain.usecase.GetAllTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetFavouriteTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetMostPlayedTracksListUseCase
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TracksPagingFlow @Inject constructor(
    private val getAllTracksListUseCase: GetAllTracksListUseCase,
    private val getFavouriteTracksListUseCase: GetFavouriteTracksListUseCase,
    private val getMostPlayedTracksListUseCase: GetMostPlayedTracksListUseCase,
) {
    fun getTracksFlow(
        pageSize: Int = PAGE_SIZE,
        tracksScreenMode: TracksScreenMode
    ): Flow<PagingData<TrackUIModel>> {
        val useCase = when (tracksScreenMode) {
            TracksScreenMode.All -> getAllTracksListUseCase
            TracksScreenMode.Favourite -> getFavouriteTracksListUseCase
            TracksScreenMode.MostPlayed -> getMostPlayedTracksListUseCase
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
            ),
            pagingSourceFactory = {
                TracksPagingSource(useCase, pageSize)
            },
        ).flow
    }
}