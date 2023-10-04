package com.ashehata.me_player.modules.home.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.ashehata.me_player.amplitude.MyAmplitude
import com.ashehata.me_player.base.BaseViewModel
import com.ashehata.me_player.modules.home.domain.usecase.GetAllTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetFavouriteTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetMostPlayedTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.UpdateTracksListUseCase
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.modules.home.presentation.contract.TracksState
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState
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
    private val getAllTracksListUseCase: GetAllTracksListUseCase,
    private val getFavouriteTracksListUseCase: GetFavouriteTracksListUseCase,
    private val getMostPlayedTracksListUseCase: GetMostPlayedTracksListUseCase,
    private val updateTracksListUseCase: UpdateTracksListUseCase,
    private val myAmplitude: MyAmplitude,
) : BaseViewModel<TracksEvent, TracksViewState, TracksState>() {

    private var myPlayer: MyPlayer? = null
    private var playbackStateJob: Job? = null

    init {
        launchCoroutine {
            val tracks = getAllTracksListUseCase.execute()
            viewStates?.allTracks = tracks
        }
    }

    override fun handleEvents(event: TracksEvent) {
        when (event) {
            is TracksEvent.AddTrackToFavourite -> {

            }

            TracksEvent.ChangeScreenMode -> {

            }

            TracksEvent.ClearAllFavourite -> {

            }

            is TracksEvent.OnTrackClicked -> {
                viewStates?.currentSelectedTrack?.value = event.trackDomainModel
                val newItem = MediaItem.Builder()
                    .setUri(event.trackDomainModel.uri)
                    .build()
                myPlayer?.iniPlayer(listOf(newItem).toMutableList())
            }

            TracksEvent.RefreshScreen -> {

            }

            is TracksEvent.RemoveTrackFromFavourite -> {

            }

            is TracksEvent.UpdateTracks -> {
                // TODO uncomment
                launchCoroutine(Dispatchers.IO) {
                    updateTracksListUseCase.execute(event.tracks/*.map {
                        it.copy(wavesList = myAmplitude.audioToWave(it.uri))
                    }*/)

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
            playbackStateJob = viewModelScope.launchPlaybackStateJob(viewStates?.playbackState, state, it)
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