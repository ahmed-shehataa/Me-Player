package com.ashehata.me_player.features.home.data.mapper

import com.ashehata.me_player.features.home.data.model.TrackDataModel
import com.ashehata.me_player.features.home.domain.model.TrackDomainModel


fun TrackDomainModel.toLocalDb(): TrackDataModel =
    TrackDataModel(
        id, name, uri, duration, size, wavesList, isFav, playingCount
    )