package com.ashehata.me_player.modules.home.data.mapper

import com.ashehata.me_player.modules.home.data.model.TrackDataModel
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel


fun TrackDataModel.toDomain(): TrackDomainModel =
    TrackDomainModel(
        id, name, uri, duration, size, wavesList
    )