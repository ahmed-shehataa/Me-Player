package com.ashehata.me_player.modules.home.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ashehata.me_player.R

enum class TracksScreenMode(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
) {
    All(R.string.all, R.drawable.ic_queue_music),
    Favourite(R.string.fav, R.drawable.ic_favorite),
    MostPlayed(R.string.most_played, R.drawable.ic_headset_mic)
}
