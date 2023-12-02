package com.ashehata.me_player.features.home.domain.model

data class TrackDomainModel(
    val id: Long? = null,
    val name: String,
    val uri: String,
    val duration: Int,
    val size: Int,
    val wavesList: List<Int> = emptyList(),
    val isFav: Boolean = false,
    val playingCount: Int = 0,
)
