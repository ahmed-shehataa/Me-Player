package com.ashehata.me_player.modules.home.presentation.pagination

import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.modules.home.domain.usecase.GetAllTracksListUseCase
import com.ashehata.me_player.modules.home.presentation.mapper.toUIModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import javax.inject.Inject

class AllTracksPagingCompose @Inject constructor(
    private val getAllTracksListUseCase: GetAllTracksListUseCase,
) : ComposePagingSource<TrackUIModel>() {

    override suspend fun loadPage(page: Int, perPage: Int): List<TrackUIModel> {
        return getAllTracksListUseCase.execute(page = page, perPage = perPage)
            .map { it.toUIModel() }
    }
}