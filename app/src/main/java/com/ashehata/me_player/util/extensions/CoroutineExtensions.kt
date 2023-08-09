package com.ashehata.me_player.util.extensions

import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.player.PlaybackState
import com.ashehata.me_player.player.PlayerStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


fun CoroutineScope.launchPlaybackStateJob(
    playbackStateFlow: MutableStateFlow<PlaybackState>?,
    state: PlayerStates,
    myPlayer: MyPlayer,
    delayToUpdate: Duration = 50.milliseconds
) = launch {
    do {
        playbackStateFlow?.emit(
            PlaybackState(
                currentPlaybackPosition = myPlayer.currentPlaybackPosition,
                currentTrackDuration = myPlayer.currentTrackDuration
            )
        )
        delay(delayToUpdate)

    } while (state == PlayerStates.STATE_PLAYING && this.isActive)
}