package com.ashehata.me_player.player

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.player.PlayerStates.End
import com.ashehata.me_player.player.PlayerStates.Ready
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A custom player class that provides several convenience methods for
 * controlling playback and monitoring the state of an underlying ExoPlayer.
 *
 * @param player The ExoPlayer instance that this class wraps.
 */
class MyPlayer(private val player: Player) : Player.Listener {

    /**
     * A state flow that emits the current playback state of the player.
     */
    val playerState: MutableStateFlow<PlayerStates> = MutableStateFlow(PlayerStates.Idel)

    /**
     * The current playback position in milliseconds. If the player's position
     * is negative, this returns 0.
     */
    val currentPlaybackPosition: Long
        get() = if (player.currentPosition > 0) player.currentPosition else 0L

    /**
     * The duration of the current track in milliseconds. If the track's duration
     * is negative, this returns 0.
     */
    val currentTrackDuration: Long
        get() = if (player.duration > 0) player.duration else 0L

    /**
     *
     */
    private val _trackList: MutableList<TrackUIModel> = mutableListOf()

    /**
     * Initializes the player with a list of media items.
     *
     * @param trackList The list of media items to play.
     */
    fun iniPlayer(trackList: List<TrackUIModel>, starterTrackPosition: Int) {
        _trackList.clear()
        _trackList.addAll(trackList)
        val mediaItems = _trackList.map { it.toMediaItem() }
        player.addListener(this)
        player.setMediaItems(mediaItems, starterTrackPosition, 0)
        player.prepare()
        player.play()
    }

    private fun TrackUIModel.toMediaItem(): MediaItem {
        return MediaItem.Builder().setUri(this.uri).build()
    }

    /**
     * Sets up the player to start playback of the track at the specified index.
     *
     * @param index The index of the track in the playlist.
     * @param isTrackPlay If true, playback will start immediately.
     */
    fun setUpTrack(index: Int, isTrackPlay: Boolean) {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.seekTo(index, 0)
        if (isTrackPlay) player.playWhenReady = true
    }

    /**
     *
     */

    fun playNext() {
        player.seekToNextMediaItem()
        player.play()
    }

    fun playPrevious() {
        player.seekToPreviousMediaItem()
        player.play()
    }

    /**
     * Toggles the playback state between playing and paused.
     */
    fun playPause() {
        if (player.playbackState == Player.STATE_IDLE) player.prepare()
        player.playWhenReady = !player.playWhenReady
    }

    /**
     * Releases the player, freeing any resources it holds.
     */
    fun releasePlayer() {
        player.release()
    }

    /**
     * Seeks to the specified position in the current track.
     *
     * @param position The position to seek to, in milliseconds.
     */
    fun seekToPosition(position: Long) {
        player.seekTo(position)
    }

    // Overrides for Player.Listener follow...

    /**
     * Called when a player error occurs. This implementation emits the
     * STATE_ERROR state to the playerState flow.
     */
    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        playerState.tryEmit(PlayerStates.Error)
    }

    /**
     * Called when the player's playWhenReady state changes. This implementation
     * emits the STATE_PLAYING or STATE_PAUSE state to the playerState flow
     * depending on the new playWhenReady state and the current playback state.
     */
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        if (player.playbackState == Player.STATE_READY) {
            if (playWhenReady) {
                val currentTrack = _trackList[player.currentPeriodIndex]
                playerState.tryEmit(PlayerStates.Playing(currentTrack))
            } else {
                playerState.tryEmit(PlayerStates.Pause)
            }
        }
    }

    /**
     * Called when the player transitions to a new media item. This implementation
     * emits the STATE_NEXT_TRACK and STATE_PLAYING states to the playerState flow
     * if the transition was automatic.
     */
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            playerState.tryEmit(PlayerStates.NextTrack)
            val currentTrack = _trackList[player.currentPeriodIndex]
            playerState.tryEmit(PlayerStates.Playing(currentTrack))
        }
    }

    /**
     * Called when the player's playback state changes. This implementation emits
     * a state to the playerState flow corresponding to the new playback state.
     */
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                playerState.tryEmit(PlayerStates.Idel)
            }

            Player.STATE_BUFFERING -> {
                playerState.tryEmit(PlayerStates.Buffering)
            }

            Player.STATE_READY -> {
                playerState.tryEmit(Ready)
                if (player.playWhenReady) {
                    val currentTrack = _trackList[player.currentPeriodIndex]
                    playerState.tryEmit(PlayerStates.Playing(currentTrack))
                } else {
                    playerState.tryEmit(PlayerStates.Pause)
                }
            }

            Player.STATE_ENDED -> {
                playerState.tryEmit(End)
            }
        }
    }
}