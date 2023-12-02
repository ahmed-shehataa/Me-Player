package com.ashehata.me_player.features.home.presentation.pagination

import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.features.home.domain.usecase.GetMostPlayedTracksListUseCase
import com.ashehata.me_player.features.home.presentation.mapper.toUIModel
import com.ashehata.me_player.features.home.presentation.model.TrackUIModel
import javax.inject.Inject

class MostPlayedTracksPagingCompose @Inject constructor(
    private val getMostPlayedTracksListUseCase: GetMostPlayedTracksListUseCase
) : ComposePagingSource<TrackUIModel>() {

    override suspend fun loadPage(page: Int, perPage: Int): List<TrackUIModel> {
        return getMostPlayedTracksListUseCase.execute(page = page, perPage = perPage)
            .map { it.toUIModel() }
    }
}