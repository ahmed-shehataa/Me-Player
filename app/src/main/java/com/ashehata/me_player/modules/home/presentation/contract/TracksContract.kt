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
import kotlinx.coroutines.flow.MutableStateFlow


sealed class TracksEvent : BaseEvent {
    data class OnTrackClicked(val trackUIModel: TrackUIModel) : TracksEvent()
    data class ToggleTrackToFavourite(val trackUIModel: TrackUIModel) : TracksEvent()
    data class UpdateTracks(val tracks: List<TrackDomainModel>) : TracksEvent()
    data class InitPlayer(val player: MyPlayer?) : TracksEvent()
    data class SeekToPosition(val position: Long) : TracksEvent()
    object PlayPauseToggle : TracksEvent()
    data class ChangeScreenMode(val tracksScreenMode: TracksScreenMode) : TracksEvent()
    object ClearAllFavourite : TracksEvent()
    object RefreshScreen : TracksEvent()
}

sealed class TracksState : BaseState {
    object AddSuccess : TracksState()
    object RemoveSuccess : TracksState()
}

data class TracksViewState(
    override val isNetworkError: MutableState<Boolean> = mutableStateOf(false),
    override val isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    override val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val isPlaying: MutableState<Boolean> = mutableStateOf(false),
    val playbackState: MutableStateFlow<PlaybackState> = MutableStateFlow(PlaybackState(0L, 0L)),
    val currentSelectedTrack: MutableState<TrackUIModel?> = mutableStateOf(null),
    val screenMode: MutableState<TracksScreenMode> = mutableStateOf(TracksScreenMode.All),
) : BaseViewState
