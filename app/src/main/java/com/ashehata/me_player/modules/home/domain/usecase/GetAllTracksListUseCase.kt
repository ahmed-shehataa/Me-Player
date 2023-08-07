package com.ashehata.me_player.modules.home.domain.usecase

import androidx.paging.PagingData
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTracksListUseCase @Inject constructor(private val tracksRepository: TracksRepository) {

    suspend fun execute(): Flow<PagingData<TrackDomainModel>> {
        return tracksRepository.getAllTracks()
    }
}