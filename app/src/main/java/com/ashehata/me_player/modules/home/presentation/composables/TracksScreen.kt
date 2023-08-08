package com.ashehata.me_player.modules.home.presentation.composables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.TracksViewModel
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TracksScreen(viewModel: TracksViewModel) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val viewStates = remember {
        viewModel.viewStates ?: TracksViewState()
    }

    val allTracks = remember {
        viewStates.allTracks
    }

    val currentSelectedTrack = remember {
        viewStates.currentSelectedTrack
    }

    val isPlaying = remember {
        viewStates.isPlaying
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

    val bottomSheetHeight =
        animateDpAsState(
            targetValue = if (currentSelectedTrack.value == null) 0.dp else 68.dp,
            animationSpec = tween(
                easing = EaseOutSine
            )
        )


    val onTrackClicked: (TrackDomainModel) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.OnTrackClicked(it))
        }
    }

    val onPlayPauseToggle: () -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.PlayPauseToggle)
        }
    }

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            TrackPlayerScreen(
                onCollapsedItemClicked = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }, currentSelectedTrack = currentSelectedTrack.value,
                onPlayPauseToggle = onPlayPauseToggle,
                isPlaying = isPlaying.value
            )
        },
        sheetPeekHeight = bottomSheetHeight.value,
        backgroundColor = MaterialTheme.colors.primary,
        sheetElevation = 4.dp,
    ) {
        TracksScreenContent(
            allTracksPagingData = allTracks,
            onTrackClicked = onTrackClicked,
            currentSelectedTrack = currentSelectedTrack.value,
        )
    }

}