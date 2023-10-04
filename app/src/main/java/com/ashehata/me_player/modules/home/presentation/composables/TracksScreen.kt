package com.ashehata.me_player.modules.home.presentation.composables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

    val playbackState = viewStates.playbackState.collectAsState()

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

    val onSeekToPosition: (Long) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.SeekToPosition(it))
        }
    }

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .statusBarsPadding()
            .navigationBarsPadding(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            PlayerScreenBottomSheet(
                onCollapsedItemClicked = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                },
                currentSelectedTrack = currentSelectedTrack.value,
                onPlayPauseToggle = onPlayPauseToggle,
                isPlaying = isPlaying.value,
                playbackState = playbackState.value,
                onSeekToPosition = onSeekToPosition
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