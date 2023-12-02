package com.ashehata.me_player.streamer

import android.util.Log
import androidx.media3.common.MediaItem
import com.ashehata.me_player.features.home.presentation.model.TracksScreenMode
import com.ashehata.me_player.features.home.presentation.pagination.AllTracksPagingCompose
import com.ashehata.me_player.features.home.presentation.pagination.FavTracksPagingCompose
import com.ashehata.me_player.features.home.presentation.pagination.MostPlayedTracksPagingCompose
import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.player.PlayerStates
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * TODO document it
 */
class Streamer<T>(
    private val player: MyPlayer,
    private var currentScreenMode: TracksScreenMode,
    private val allTracksPagingCompose: AllTracksPagingCompose,
    private val favTracksPagingCompose: FavTracksPagingCompose,
    private val mostPlayedTracksPagingCompose: MostPlayedTracksPagingCompose
) {
    var streamerState: MutableStateFlow<PlayerStates> = player.playerState

    init {
        //player.init()
    }


    /**
     * TODO fix to be more efficient
     */
    fun playTrack(
        tracksScreenMode: TracksScreenMode,
        trackPositionInList: Int,
        start: Boolean
    ) {
        val mediaItems: List<MediaItem> = when (tracksScreenMode) {
            TracksScreenMode.All -> {
                allTracksPagingCompose.list.map { it.mediaItem }
            }

            TracksScreenMode.Favourite -> {
                favTracksPagingCompose.list.map { it.mediaItem }
            }

            TracksScreenMode.MostPlayed -> {
                mostPlayedTracksPagingCompose.list.map { it.mediaItem }
            }
        }

        Log.i("playTrack", "mediaItems: size " + mediaItems.size)
        Log.i("playTrack", "tracksScreenMode: " + tracksScreenMode.name)
        player.apply {
            init()
            //clearMediaItems()
            setMediaItems(mediaItems, trackPositionInList, 0)
            prepare()
            if (start)
                play()
        }

    }

    fun togglePlay() {
        player.playPause()
    }

    fun seekToPosition(position: Long) {
        player.seekToPosition(position)
    }

    fun playNext() {
        player.playNext()
    }

    fun playPrevious() {
        player.playPrevious()
    }

    fun release() {
        player.releasePlayer()
    }

    fun getCurrentPlaybackPosition() = player.currentPlaybackPosition
    fun getCurrentTrackDuration() = player.currentTrackDuration

}