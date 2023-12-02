package com.ashehata.me_player.features.home.domain.usecase

import com.ashehata.me_player.features.home.domain.model.TrackDomainModel
import com.ashehata.me_player.features.home.domain.repository.TracksRepository
import javax.inject.Inject

class GetFavouriteTracksListUseCase @Inject constructor(private val tracksRepository: TracksRepository) :
    ITracksUseCase {

    override suspend fun execute(page: Int, perPage: Int): List<TrackDomainModel> {
        return tracksRepository.getFavouriteTracks(page, perPage)
    }
}