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
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import com.ashehata.me_player.modules.home.presentation.pagination.AllTracksPagingCompose
import com.ashehata.me_player.modules.home.presentation.pagination.FavTracksPagingCompose
import com.ashehata.me_player.modules.home.presentation.pagination.MostPlayedTracksPagingCompose
import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.player.PlayerStates
import com.ashehata.me_player.streamer.Streamer
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

    private var playbackStateJob: Job? = null
    lateinit var streamer: Streamer<TrackUIModel>
    /* private var notifyPlayerWithNewList: (List<TrackUIModel>) -> Unit = {
         myPlayer?.appendList(it)
     }*/

    init {
        /* // Notify the player after loading next page
         listOf(
             allTracksPagingCompose,
             favTracksPagingCompose,
             mostPlayedTracksPagingCompose
         ).forEach {
             it.onNewPageLoaded = notifyPlayerWithNewList
         }*/
    }

    override fun handleEvents(event: TracksEvent) {
        when (event) {
            is TracksEvent.ChangeScreenMode -> {
                Log.i("ChangeScreenMode", "handleEvents: " + event.tracksScreenMode.name)
                viewStates?.screenMode?.value = event.tracksScreenMode
            }

            TracksEvent.ClearAllFavourite -> {

            }

            is TracksEvent.ToggleTrackToFavourite -> {
                launchCoroutine {
                    toggleToFav(event.trackUIModel)
                }
            }

            is TracksEvent.UpdateTracks -> {
                // TODO UpdateTracks if needed
                launchCoroutine(Dispatchers.IO) {
                    updateTracksListUseCase.execute(event.tracks)
                    listOf(
                        allTracksPagingCompose,
                        favTracksPagingCompose,
                        mostPlayedTracksPagingCompose
                    ).forEach { it.refresh() }
                }
            }

            is TracksEvent.InitPlayer -> {
                event.player?.let {
                    if (::streamer.isInitialized.not())
                        initStreamer(event.player)
                }
            }

            TracksEvent.PlayPauseToggle -> {
                streamer.togglePlay()
            }

            is TracksEvent.SeekToPosition -> {
                streamer.seekToPosition(event.position)
            }

            is TracksEvent.PlayTrackAtPosition -> {
                Log.i("handleEvents: ", "PlayTrackAtPosition")
                viewStates?.currentSelectedTrack?.value = event.track
                // change bottomSheetMode to display the correct playing -> paging source data
                if (getCurrentScreenMode() != viewStates?.bottomSheetMode?.value) {
                    viewStates?.bottomSheetMode?.value = getCurrentScreenMode()
                }

                val startPlaying = if (event.force) true else ifPlayerPaused().not()

                if (startPlaying) {
                    increasePlayingCount(event.track)
                }
                streamer.playTrack(
                    tracksScreenMode = getCurrentScreenMode(),
                    trackPositionInList = event.position,
                    start = startPlaying
                )

            }

            TracksEvent.OpenBottomSheet -> {
                setState {
                    TracksState.ExpandBottomSheet
                }
            }
        }
    }

    private fun increasePlayingCount(track: TrackUIModel) {
        launchCoroutine {
            val updatedTrack = track.apply { playingCount += 1 }
            updateTrackUseCase.execute(updatedTrack.toDomain())
        }
    }

    private fun ifPlayerPaused(): Boolean {
        return viewStates?.playerState?.value is PlayerStates.Pause
    }

    private fun initStreamer(player: MyPlayer) {
        launchCoroutine(Dispatchers.Main) {

            streamer = Streamer(
                currentScreenMode = getCurrentScreenMode(),
                player = player,
                allTracksPagingCompose = allTracksPagingCompose,
                favTracksPagingCompose = favTracksPagingCompose,
                mostPlayedTracksPagingCompose = mostPlayedTracksPagingCompose
            )

            streamer.streamerState.collectLatest {
                Log.i("playerState: ", it.toString())
                updatePlaybackState(it)
                viewStates?.playerState?.value = it
                if (viewStates?.playerState?.value is PlayerStates.Playing) {
                    val index = (it as PlayerStates.Playing).currentTrackIndex
                    viewStates?.currentSelectedTrack?.value =
                        getCurrentPlayedTrack(index)
                }
            }
        }
    }

    private fun getCurrentPlayedTrack(index: Int): TrackUIModel? {
        return when (viewStates?.screenMode?.value) {
            TracksScreenMode.All -> {
                allTracksPagingCompose.list[index]
            }

            TracksScreenMode.Favourite -> {
                favTracksPagingCompose.list[index]
            }

            TracksScreenMode.MostPlayed -> {
                mostPlayedTracksPagingCompose.list[index]
            }

            null -> {
                null
            }
        }
    }

    private fun getCurrentScreenMode(): TracksScreenMode {
        return viewStates?.screenMode?.value ?: TracksScreenMode.All
    }

    private suspend fun toggleToFav(trackUIModel: TrackUIModel) {
        val updatedTrack = trackUIModel.copy(isFav = trackUIModel.isFav.not())
        updateTrackUseCase.execute(updatedTrack.toDomain())

        // Update All tracks ui list
        allTracksPagingCompose.updateItem(
            oldItem = trackUIModel,
            newItem = updatedTrack
        )

        // Update Most played tracks ui list
        mostPlayedTracksPagingCompose.updateItem(
            oldItem = trackUIModel,
            newItem = updatedTrack
        )

        // Update Fav tracks ui list
        if (updatedTrack.isFav) {
            favTracksPagingCompose.addItem(updatedTrack)
        } else {
            favTracksPagingCompose.removeItem(trackUIModel)
        }

        // Update current playing track
        if (viewStates?.currentSelectedTrack?.value == trackUIModel) {
            viewStates?.currentSelectedTrack?.value = updatedTrack
        }
    }

    private fun updatePlaybackState(state: PlayerStates) {
        playbackStateJob?.cancel()
        playbackStateJob =
            viewModelScope.launchPlaybackStateJob(viewStates?.playbackState, state, streamer)
    }

    override fun createInitialViewState(): TracksViewState {
        return TracksViewState()
    }

    override fun onCleared() {
        super.onCleared()
        streamer.release()
    }
}