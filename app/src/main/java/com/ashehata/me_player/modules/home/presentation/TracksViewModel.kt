package com.ashehata.me_player.modules.home.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.cachedIn
import com.ashehata.me_player.amplitude.MyAmplitude
import com.ashehata.me_player.base.BaseViewModel
import com.ashehata.me_player.modules.home.domain.usecase.UpdateTrackUseCase
import com.ashehata.me_player.modules.home.domain.usecase.UpdateTracksListUseCase
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.modules.home.presentation.contract.TracksState
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState
import com.ashehata.me_player.modules.home.presentation.mapper.toDomain
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import com.ashehata.me_player.modules.home.presentation.pagination.AllTracksPagingCompose
import com.ashehata.me_player.modules.home.presentation.pagination.TracksPagingFlow
import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.player.PlayerStates
import com.ashehata.me_player.util.extensions.launchPlaybackStateJob
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val tracksPagingFlow: TracksPagingFlow,
    private val updateTracksListUseCase: UpdateTracksListUseCase,
    private val updateTrackUseCase: UpdateTrackUseCase,
    private val myAmplitude: MyAmplitude,
    val allTracksPagingCompose: AllTracksPagingCompose
) : BaseViewModel<TracksEvent, TracksViewState, TracksState>() {

    /* val allTracks: Flow<PagingData<TrackUIModel>> = tracksPagingFlow.getTracksFlow(tracksScreenMode = TracksScreenMode.All)
         .cachedIn(viewModelScope)*/

    val favouriteTracks =
        tracksPagingFlow.getTracksFlow(tracksScreenMode = TracksScreenMode.Favourite)
            .cachedIn(viewModelScope)
    val mostPlayedTracks =
        tracksPagingFlow.getTracksFlow(tracksScreenMode = TracksScreenMode.MostPlayed)
            .cachedIn(viewModelScope)
    private var myPlayer: MyPlayer? = null
    private var playbackStateJob: Job? = null


    override fun handleEvents(event: TracksEvent) {
        when (event) {
            is TracksEvent.ChangeScreenMode -> {
                Log.i("ChangeScreenMode", "handleEvents: " + event.tracksScreenMode.name)
                viewStates?.screenMode?.value = event.tracksScreenMode
            }

            TracksEvent.ClearAllFavourite -> {

            }

            is TracksEvent.OnTrackClicked -> {
                viewStates?.currentSelectedTrack?.value = event.trackUIModel
                val newItem = MediaItem.Builder()
                    .setUri(event.trackUIModel.uri)
                    .build()
                myPlayer?.iniPlayer(listOf(newItem).toMutableList())
            }

            TracksEvent.RefreshScreen -> {

            }

            is TracksEvent.ToggleTrackToFavourite -> {
                launchCoroutine {
                    val newTrack = event.trackUIModel.copy(isFav = event.trackUIModel.isFav.not())
                    updateTrackUseCase.execute(newTrack.toDomain())
                    allTracksPagingCompose.updateItem(
                        oldItem = event.trackUIModel,
                        newItem = newTrack
                    )
                }
            }

            is TracksEvent.UpdateTracks -> {
                // TODO uncomment
                launchCoroutine(Dispatchers.IO) {
                    updateTracksListUseCase.execute(
                        event.tracks/*.map {
                        it.copy(wavesList = myAmplitude.audioToWave(it.uri))
                    }*/
                    )

                }
            }

            is TracksEvent.InitPlayer -> {
                launchCoroutine(Dispatchers.Main) {
                    myPlayer = event.player
                    myPlayer?.playerState?.collectLatest {
                        Log.i("handleEvents: ", it.name)
                        updatePlaybackState(it)
                        viewStates?.isPlaying?.value = it == PlayerStates.STATE_PLAYING

                    }
                }
                //listenToProgressUpdates()
            }

            TracksEvent.PlayPauseToggle -> {
                myPlayer?.playPause()
            }

            is TracksEvent.SeekToPosition -> {
                myPlayer?.seekToPosition(event.position)
            }
        }
    }

    private fun updatePlaybackState(state: PlayerStates) {
        playbackStateJob?.cancel()
        myPlayer?.let {
            playbackStateJob =
                viewModelScope.launchPlaybackStateJob(viewStates?.playbackState, state, it)
        }
    }

    override fun createInitialViewState(): TracksViewState {
        return TracksViewState()
    }

    override fun onCleared() {
        super.onCleared()
        myPlayer?.releasePlayer()
    }
}