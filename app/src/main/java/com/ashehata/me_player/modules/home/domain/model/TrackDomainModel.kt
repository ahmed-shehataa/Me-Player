package com.ashehata.me_player.modules.home.domain.model

data class TrackDomainModel(
    val id: Long,
    val name: String,
    val uri: String,
    val duration: Long,
    val size: Long,
)
