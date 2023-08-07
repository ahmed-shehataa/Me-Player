package com.ashehata.me_player.util.extensions



fun Int.toTimeFormat() : String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> String.format("%02d : %02d : %02d", hours, minutes, seconds)
        else -> String.format("%02d : %02d", minutes, seconds)
    }
}