package com.ashehata.me_player.modules.home.presentation

import com.ashehata.me_player.base.BaseViewModel
import com.ashehata.me_player.modules.home.domain.usecase.GetAllTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetFavouriteTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.GetMostPlayedTracksListUseCase
import com.ashehata.me_player.modules.home.domain.usecase.UpdateTracksListUseCase
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.modules.home.presentation.contract.TracksState
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val getAllTracksListUseCase: GetAllTracksListUseCase,
    private val getFavouriteTracksListUseCase: GetFavouriteTracksListUseCase,
    private val getMostPlayedTracksListUseCase: GetMostPlayedTracksListUseCase,
    private val updateTracksListUseCase: UpdateTracksListUseCase,
) : BaseViewModel<TracksEvent, TracksViewState, TracksState>() {


    init {
        launchCoroutine {
            viewStates?.allTracks = getAllTracksListUseCase.execute()
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
        }
    }


    override fun createInitialViewState(): TracksViewState {
        return TracksViewState()
    }
}