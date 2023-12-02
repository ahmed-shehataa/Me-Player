package com.ashehata.me_player.features.home.presentation.pagination

import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.features.home.domain.usecase.GetFavouriteTracksListUseCase
import com.ashehata.me_player.features.home.presentation.mapper.toUIModel
import com.ashehata.me_player.features.home.presentation.model.TrackUIModel
import javax.inject.Inject

class FavTracksPagingCompose @Inject constructor(
    private val getFavouriteTracksListUseCase: GetFavouriteTracksListUseCase
) : ComposePagingSource<TrackUIModel>() {

    override suspend fun loadPage(page: Int, perPage: Int): List<TrackUIModel> {
        return getFavouriteTracksListUseCase.execute(page = page, perPage = perPage)
            .map { it.toUIModel() }
    }
}