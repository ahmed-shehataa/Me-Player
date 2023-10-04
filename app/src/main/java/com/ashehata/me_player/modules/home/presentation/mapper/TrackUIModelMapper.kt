package com.ashehata.me_player.modules.home.presentation.mapper

import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel


fun TrackDomainModel.toUIModel(): TrackUIModel =
    TrackUIModel(
        id, name, uri, duration, size, wavesList, isFav, playingCount
    )

fun TrackUIModel.toDomain(): TrackDomainModel =
    TrackDomainModel(
        id, name, uri, duration, size, wavesList, isFav, playingCount
    )
