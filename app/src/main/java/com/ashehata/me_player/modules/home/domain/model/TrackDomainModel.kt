package com.ashehata.me_player.modules.home.domain.model

data class TrackDomainModel(
    val id: Long? = null,
    val name: String,
    val uri: String,
    val duration: Int,
    val size: Int,
    val wavesList: List<Int> = emptyList(),
)
