package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ashehata.me_player.modules.home.presentation.TracksViewModel
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState


@Composable
fun TracksScreen(viewModel: TracksViewModel) {
    val context = LocalContext.current

    val viewStates = remember {
        viewModel.viewStates ?: TracksViewState()
    }

    val allTracks = remember {
        viewStates.allTracks
    }

    val isLoading = remember {
        viewStates.isLoading
    }

    val isRefreshing = remember {
        viewStates.isRefreshing
    }

    val screenMode = remember {
        viewStates.screenMode
    }

    val isNetworkError = remember {
        viewStates.isNetworkError
    }


    TracksScreenContent (
        allTracksPagingData = allTracks
    )
}