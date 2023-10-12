package com.ashehata.me_player.modules.home.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
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
import com.ashehata.me_player.modules.home.presentation.pagination.FavTracksPagingCompose
import com.ashehata.me_player.modules.home.presentation.pagination.MostPlayedTracksPagingCompose
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
    private val updateTracksListUseCase: UpdateTracksListUseCase,
    private val updateTrackUseCase: UpdateTrackUseCase,
    private val myAmplitude: MyAmplitude,
    val allTracksPagingCompose: AllTracksPagingCompose,
    val favTracksPagingCompose: FavTracksPagingCompose,
    val mostPlayedTracksPagingCompose: MostPlayedTracksPagingCompose,
) : BaseViewModel<TracksEvent, TracksViewState, TracksState>() {

    private var myPlayer: MyPlayer? = null
    private var playbackStateJob: Job? = null


    override fun handleEvents(event: TracksEvent) {
        when (event) {
            is TracksEvent.ChangeScreenMode -> {
                viewStates?.screenMode?.value = event.tracksScreenMode
            }

            TracksEvent.ClearAllFavourite -> {

            }

            is TracksEvent.OnTrackClicked -> {
                //viewStates?.currentSelectedTrack?.value = event.trackUIModel
                val tracksToPlay = when (viewStates?.screenMode?.value) {
                    TracksScreenMode.All -> allTracksPagingCompose.list
                    TracksScreenMode.Favourite -> favTracksPagingCompose.list
                    TracksScreenMode.MostPlayed -> mostPlayedTracksPagingCompose.list
                    else -> emptyList()
                }
                val trackIndex = tracksToPlay.indexOf(event.trackUIModel)
                myPlayer?.iniPlayer(tracksToPlay, trackIndex)
            }

            TracksEvent.RefreshScreen -> {

            }

            is TracksEvent.ToggleTrackToFavourite -> {
                launchCoroutine {
                    val updatedTrack =
                        event.trackUIModel.copy(isFav = event.trackUIModel.isFav.not())
                    updateTrackUseCase.execute(updatedTrack.toDomain())

                    // Update All tracks ui list
                    allTracksPagingCompose.updateItem(
                        oldItem = event.trackUIModel,
                        newItem = updatedTrack
                    )

                    // Update Most played tracks ui list
                    mostPlayedTracksPagingCompose.updateItem(
                        oldItem = event.trackUIModel,
                        newItem = updatedTrack
                    )

                    // Update Fav tracks ui list
                    if (updatedTrack.isFav) {
                        favTracksPagingCompose.addItem(updatedTrack)
                    } else {
                        favTracksPagingCompose.removeItem(event.trackUIModel)
                    }

                    // Update current playing track
                    if (viewStates?.currentSelectedTrack?.value == event.trackUIModel) {
                        viewStates?.currentSelectedTrack?.value = updatedTrack
                    }
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
                        Log.i("playerState: ", it.toString())
                        updatePlaybackState(it)
                        viewStates?.playerState?.value = it
                        if (viewStates?.playerState?.value is PlayerStates.Playing) {
                            viewStates?.currentSelectedTrack?.value =
                                (it as PlayerStates.Playing).currentTrack
                        }
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