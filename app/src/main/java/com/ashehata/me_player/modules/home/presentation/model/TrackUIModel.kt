package com.ashehata.me_player.modules.home.presentation.model

data class TrackUIModel(
    val id: Long? = null,
    val name: String = "",
    val uri: String = "",
    val duration: Int = -1,
    val size: Int = -1,
    val wavesList: List<Int> = emptyList(),
    val isFav: Boolean = false,
    val playingCount: Int = -1,
)
