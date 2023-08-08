package com.ashehata.me_player.modules.home.presentation

import android.util.Log
import androidx.media3.common.MediaItem
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val getAllTracksListUseCase: GetAllTracksListUseCase,
    private val getFavouriteTracksListUseCase: GetFavouriteTracksListUseCase,
    private val getMostPlayedTracksListUseCase: GetMostPlayedTracksListUseCase,
    private val updateTracksListUseCase: UpdateTracksListUseCase,
    private val myPlayer: MyPlayer,
) : BaseViewModel<TracksEvent, TracksViewState, TracksState>() {

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
                myPlayer?.let {
                    it.iniPlayer(listOf(newItem).toMutableList())
                }
            }

            TracksEvent.RefreshScreen -> {

            }

            is TracksEvent.RemoveTrackFromFavourite -> {

            }

            is TracksEvent.UpdateTracks -> {
                launchCoroutine {
                    updateTracksListUseCase.execute(event.tracks)

                }
            }

            is TracksEvent.InitPlayer -> {
                launchCoroutine {
                    myPlayer.playerState.collectLatest {
                        Log.i("handleEvents: ", it.name)
                        viewStates?.isPlaying?.value = it == PlayerStates.STATE_PLAYING
                    }
                }
            }

            TracksEvent.PlayPauseToggle -> {
                myPlayer.playPause()
            }
        }
    }


    override fun createInitialViewState(): TracksViewState {
        return TracksViewState()
    }

    override fun onCleared() {
        super.onCleared()
        //myPlayer.releasePlayer()
    }
}