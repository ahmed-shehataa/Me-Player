package com.ashehata.me_player.modules.home.presentation.model

import androidx.media3.common.MediaItem

data class TrackUIModel(
    val id: Long? = null,
    val name: String = "",
    val uri: String = "",
    val duration: Int = -1,
    val size: Int = -1,
    val wavesList: List<Int> = emptyList(),
    val isFav: Boolean = false,
    val playingCount: Int = -1,
    val mediaItem: MediaItem = MediaItem.Builder().setUri(uri).build()
)
