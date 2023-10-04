package com.ashehata.me_player.modules.home.domain.usecase

import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import javax.inject.Inject

class GetAllTracksListUseCase @Inject constructor(private val tracksRepository: TracksRepository) :
    ITracksUseCase {

    override suspend fun execute(page: Int, perPage: Int): List<TrackDomainModel> {
        return tracksRepository.getAllTracks(page, perPage)
    }
}