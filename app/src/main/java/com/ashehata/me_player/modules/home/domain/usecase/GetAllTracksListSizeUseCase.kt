package com.ashehata.me_player.modules.home.domain.usecase

import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import javax.inject.Inject

class GetAllTracksListSizeUseCase @Inject constructor(private val tracksRepository: TracksRepository) {

    suspend fun execute(): Int {
        return tracksRepository.getAllTracksSize()
    }
}