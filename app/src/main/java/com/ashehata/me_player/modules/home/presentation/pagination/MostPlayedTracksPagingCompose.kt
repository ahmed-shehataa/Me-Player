package com.ashehata.me_player.modules.home.presentation.pagination

import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.modules.home.domain.usecase.GetAllTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetFavouriteTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetMostPlayedTracksListUseCase
import com.ashehata.me_player.modules.home.presentation.mapper.toUIModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import javax.inject.Inject

class MostPlayedTracksPagingCompose @Inject constructor(
    private val getMostPlayedTracksListUseCase: GetMostPlayedTracksListUseCase
) : ComposePagingSource<TrackUIModel>() {

    override suspend fun loadPage(page: Int, perPage: Int): List<TrackUIModel> {
        return getMostPlayedTracksListUseCase.execute(page = page, perPage = perPage)
            .map { it.toUIModel() }
    }
}