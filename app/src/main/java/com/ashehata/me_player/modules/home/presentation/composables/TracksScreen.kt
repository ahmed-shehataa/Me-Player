package com.ashehata.me_player.modules.home.presentation.composables

import android.util.Log
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
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.modules.home.presentation.TracksViewModel
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TracksScreen(viewModel: TracksViewModel) {

    val scope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val viewStates = remember {
        viewModel.viewStates ?: TracksViewState()
    }

    val currentSelectedTrack = remember {
        viewStates.currentSelectedTrack
    }

    val playbackState = viewStates.playbackState.collectAsState()

    val isPlaying = remember {
        viewStates.isPlaying
    }

    val screenMode = remember {
        viewStates.screenMode
    }

    val bottomSheetHeight =
        animateDpAsState(
            targetValue = if (currentSelectedTrack.value == null) 0.dp else 68.dp,
            animationSpec = tween(
                easing = EaseOutSine
            )
        )


    val onTrackClicked: (TrackUIModel) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.OnTrackClicked(it))
        }
    }

    val toggleTrackToFavourite: (TrackUIModel) -> Unit = remember {
        {
            Log.i("toggleTrackToFavourite", "TracksScreen: ")
            viewModel.setEvent(TracksEvent.ToggleTrackToFavourite(it))
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

    val onChangeScreenMode: (TracksScreenMode) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.ChangeScreenMode(it))
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
                onSeekToPosition = onSeekToPosition,
                toggleTrackToFavourite = toggleTrackToFavourite
            )
        },
        sheetPeekHeight = bottomSheetHeight.value,
        backgroundColor = MaterialTheme.colors.primary,
        sheetElevation = 4.dp,
    ) {
        TracksScreenContent(
            allTracksPagingData = viewModel.allTracksPagingCompose,
            favouriteTracksPagingData = viewModel.favTracksPagingCompose,
            mostPlayedTracksPagingData = viewModel.mostPlayedTracksPagingCompose,
            onTrackClicked = onTrackClicked,
            currentSelectedTrack = currentSelectedTrack.value,
            screenMode = screenMode.value,
            onChangeScreenMode = onChangeScreenMode,
            toggleTrackToFavourite = toggleTrackToFavourite,
            bottomPadding = it.calculateBottomPadding()
        )
    }

}