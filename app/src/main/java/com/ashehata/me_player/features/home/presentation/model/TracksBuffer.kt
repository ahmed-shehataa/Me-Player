package com.ashehata.me_player.features.home.presentation.model

data class TracksBuffer(
    val isBuffering: Boolean,
    val totalCount: Int,
    val buffered: Int,

) {
    fun getPercentage(): Float = buffered.toFloat() / totalCount.toFloat()
}
