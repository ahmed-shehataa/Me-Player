package com.ashehata.me_player.player

import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel

/**
 * An enumeration of the possible states for the player.
 */
sealed class PlayerStates() {
    /**
     * State when the player is idle, not ready to play.
     */
    object Idel : PlayerStates()

    /**
     * State when the player is ready to start playback.
     */
    object Ready : PlayerStates()

    /**
     * State when the player is buffering content.
     */
    object Buffering : PlayerStates()

    /**
     * State when the player has encountered an error.
     */
    object Error : PlayerStates()

    /**
     * State when the playback has ended.
     */
    object End : PlayerStates()

    /**
     * State when the player is actively playing content.
     */
    data class Playing(val currentTrackIndex: Int) : PlayerStates()

    /**
     * State when the player has paused the playback.
     */
    object Pause : PlayerStates()

    /**
     * State when the player has moved to the next track.
     */
    object NextTrack : PlayerStates()

}