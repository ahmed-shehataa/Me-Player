package com.ashehata.me_player.navigation

sealed class HomeNavigation(val route: String) {
    object Notification : HomeNavigation("notification")
    object Media : HomeNavigation("media")
    object Home : HomeNavigation("home")
}
