package com.ashehata.me_player.modules.home.domain.usecase

import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import javax.inject.Inject

class InsertTrackUseCase @Inject constructor(private val tracksRepository: TracksRepository) {
    suspend fun execute(trackDomainModel: TrackDomainModel) {
        return tracksRepository.insertTrack(trackDomainModel)
    }
}