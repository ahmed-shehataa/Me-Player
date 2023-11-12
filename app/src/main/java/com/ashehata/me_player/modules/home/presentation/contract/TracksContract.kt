package com.ashehata.me_player.modules.home.presentation.contract

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ashehata.me_player.base.BaseEvent
import com.ashehata.me_player.base.BaseState
import com.ashehata.me_player.base.BaseViewState
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.player.PlaybackState
import com.ashehata.me_player.player.PlayerStates
import kotlinx.coroutines.flow.MutableStateFlow


sealed class TracksEvent : BaseEvent {
    data class ToggleTrackToFavourite(val trackUIModel: TrackUIModel) : TracksEvent()
    data class UpdateTracks(val tracks: List<TrackDomainModel>) : TracksEvent()
    data class InitPlayer(val player: MyPlayer?) : TracksEvent()
    data class SeekToPosition(val position: Long) : TracksEvent()
    object PlayPauseToggle : TracksEvent()
    object OpenBottomSheet : TracksEvent()
    data class PlayTrackAtPosition(val position: Int, val track: TrackUIModel, val force: Boolean) : TracksEvent()
    data class ChangeScreenMode(val tracksScreenMode: TracksScreenMode) : TracksEvent()
    object ClearAllFavourite : TracksEvent()
}

sealed class TracksState : BaseState {
    object ExpandBottomSheet : TracksState()
    data class ScrollToIndex(val index: Int) : TracksState()
}

data class TracksViewState(
    val playerState: MutableState<PlayerStates> = mutableStateOf(PlayerStates.Idel),
    val playbackState: MutableStateFlow<PlaybackState> = MutableStateFlow(PlaybackState(0L, 0L)),
    val currentSelectedTrack: MutableState<TrackUIModel?> = mutableStateOf(null),
    val screenMode: MutableState<TracksScreenMode> = mutableStateOf(TracksScreenMode.All),
    val bottomSheetMode: MutableState<TracksScreenMode> = mutableStateOf(screenMode.value),
) : BaseViewState()
