package com.ashehata.me_player.modules.home.presentation.model

data class TrackUIModel(
    val id: Long? = null,
    val name: String,
    val uri: String,
    val duration: Int,
    val size: Int,
    val wavesList: List<Int> = emptyList(),
    val isFav: Boolean,
    val playingCount: Int,
)
