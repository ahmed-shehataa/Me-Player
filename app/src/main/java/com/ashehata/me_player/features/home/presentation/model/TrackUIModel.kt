package com.ashehata.me_player.features.home.presentation.model

import androidx.media3.common.MediaItem
import com.ashehata.me_player.common.models.PaginatedItem

data class TrackUIModel(
    val id: Long? = null,
    val name: String = "",
    val uri: String = "",
    val duration: Int = -1,
    val size: Int = -1,
    val wavesList: List<Int> = emptyList(),
    val isFav: Boolean = false,
    var playingCount: Int = -1,
    val mediaItem: MediaItem = MediaItem.Builder().setUri(uri).build()
) : PaginatedItem {
    override fun getId(): Int {
       return id?.toInt() ?: -1
    }
}
