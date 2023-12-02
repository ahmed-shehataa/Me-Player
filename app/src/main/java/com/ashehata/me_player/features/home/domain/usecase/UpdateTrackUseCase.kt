package com.ashehata.me_player.features.home.domain.usecase

import com.ashehata.me_player.features.home.domain.model.TrackDomainModel
import com.ashehata.me_player.features.home.domain.repository.TracksRepository
import javax.inject.Inject

class UpdateTrackUseCase @Inject constructor(private val tracksRepository: TracksRepository) {
    suspend fun execute(trackDomainModel: TrackDomainModel) {
        return tracksRepository.updateTrack(trackDomainModel)
    }
}