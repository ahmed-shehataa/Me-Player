package com.ashehata.me_player.features.home.domain.usecase

import com.ashehata.me_player.features.home.domain.model.TrackDomainModel
import com.ashehata.me_player.features.home.domain.repository.TracksRepository
import javax.inject.Inject

class GetAllTracksUseCase @Inject constructor(private val tracksRepository: TracksRepository)
     {

     suspend fun execute(): List<TrackDomainModel> {
        return tracksRepository.getAllTracks()
    }
}