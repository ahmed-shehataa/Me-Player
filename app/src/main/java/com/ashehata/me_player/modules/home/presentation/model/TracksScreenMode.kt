package com.ashehata.me_player.modules.home.presentation.model

sealed class TracksScreenMode {
    object All : TracksScreenMode()
    object Favourite : TracksScreenMode()
    object MostPlayed : TracksScreenMode()
}
