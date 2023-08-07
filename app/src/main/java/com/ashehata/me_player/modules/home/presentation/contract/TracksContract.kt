package com.ashehata.me_player.modules.home.presentation.contract

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.ashehata.me_player.base.BaseEvent
import com.ashehata.me_player.base.BaseState
import com.ashehata.me_player.base.BaseViewState
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import kotlinx.coroutines.flow.Flow


sealed class TracksEvent : BaseEvent {
    data class OnTrackClicked(val trackDomainModel: TrackDomainModel) : TracksEvent()
    data class AddTrackToFavourite(val trackDomainModel: TrackDomainModel) : TracksEvent()
    data class RemoveTrackFromFavourite(val trackDomainModel: TrackDomainModel) : TracksEvent()
    data class UpdateTracks(val tracks: List<TrackDomainModel>) : TracksEvent()
    object ChangeScreenMode : TracksEvent()
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
    val screenMode: MutableState<TracksScreenMode> = mutableStateOf(TracksScreenMode.All),
    var allTracks: Flow<PagingData<TrackDomainModel>>? = null
) : BaseViewState
