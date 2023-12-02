package com.ashehata.me_player.util.extensions

import com.ashehata.me_player.features.home.presentation.model.TrackUIModel
import com.ashehata.me_player.player.PlaybackState
import com.ashehata.me_player.player.PlayerStates
import com.ashehata.me_player.streamer.Streamer
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
    streamer: Streamer<TrackUIModel>,
    delayToUpdate: Duration = 50.milliseconds
) = launch {
    do {
        playbackStateFlow?.emit(
            PlaybackState(
                currentPlaybackPosition = streamer.getCurrentPlaybackPosition(),
                currentTrackDuration = streamer.getCurrentTrackDuration()
            )
        )
        delay(delayToUpdate)

    } while (state is PlayerStates.Playing && this.isActive)
}