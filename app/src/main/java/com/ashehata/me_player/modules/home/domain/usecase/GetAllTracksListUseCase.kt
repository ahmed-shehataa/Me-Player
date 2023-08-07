package com.ashehata.me_player.modules.home.domain.usecase

import androidx.paging.PagingSource
import com.ashehata.me_player.modules.home.data.model.TrackDataModel
import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import javax.inject.Inject

class GetAllTracksListUseCase @Inject constructor(private val tracksRepository: TracksRepository) {

    suspend fun execute(): PagingSource<Int, TrackDataModel> {
        return tracksRepository.getAllTracks()
    }
}