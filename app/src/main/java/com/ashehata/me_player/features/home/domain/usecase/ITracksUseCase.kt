package com.ashehata.me_player.features.home.domain.usecase

import com.ashehata.me_player.features.home.domain.model.TrackDomainModel

interface ITracksUseCase {

    suspend fun execute(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel>
}