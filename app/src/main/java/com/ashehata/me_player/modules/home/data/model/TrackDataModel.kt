package com.ashehata.me_player.modules.home.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tracks")
data class TrackDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val uri: String,
    val duration: Int,
    val size: Int,
    val wavesList: List<Int>,
)
