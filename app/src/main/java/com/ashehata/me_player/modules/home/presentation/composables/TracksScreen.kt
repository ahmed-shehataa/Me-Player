package com.ashehata.me_player.modules.home.presentation.composables

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.common.presentation.compose.PaginatedHorizontalPager
import com.ashehata.me_player.modules.home.presentation.TracksViewModel
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.modules.home.presentation.contract.TracksViewState
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TracksScreen(viewModel: TracksViewModel) {

    /**
     * States
     */
    val scope = rememberCoroutineScope()

    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed))

    val pagerState = rememberPagerState()

    val viewStates = remember { viewModel.viewStates ?: TracksViewState() }

    val currentSelectedTrack = remember { viewStates.currentSelectedTrack }

    val playbackState = viewStates.playbackState.collectAsState()

    val playerState = remember { viewStates.playerState }

    val screenMode = remember { viewStates.screenMode }

    val bottomSheetMode = remember { viewStates.bottomSheetMode }

    val bottomSheetPagingSource = remember(bottomSheetMode.value) {
        derivedStateOf {
            Log.i("bottomSheetPagingSource", "TracksScreen: " + bottomSheetMode.value)
            when (bottomSheetMode.value) {
                TracksScreenMode.All -> viewModel.allTracksPagingCompose
                TracksScreenMode.Favourite -> viewModel.favTracksPagingCompose
                TracksScreenMode.MostPlayed -> viewModel.mostPlayedTracksPagingCompose
            }
        }
    }

    val bottomSheetHeight =
        animateDpAsState(
            targetValue = if (currentSelectedTrack.value == null) 0.dp else 68.dp,
            animationSpec = tween(
                easing = EaseOutSine
            )
        )

    /**
     * Actions
     */
    val onTrackClicked: (TrackUIModel, Int) -> Unit = remember {
        { track, index ->
            if (currentSelectedTrack.value == null)
                currentSelectedTrack.value = TrackUIModel()
            else
                scope.launch { pagerState.scrollToPage(index) }
        }
    }

    val toggleTrackToFavourite: (TrackUIModel) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.ToggleTrackToFavourite(it))
        }
    }

    val onNextClicked: () -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.PlayNextTrack)
            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
        }
    }

    val onPreviousClicked: () -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.PlayPreviousTrack)
            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
        }
    }

    val onPlayTrackAtPosition: (Int) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.PlayTrackAtPosition(it))
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
            if (currentSelectedTrack.value != null)
                PaginatedHorizontalPager(
                    composePagingSource = bottomSheetPagingSource.value,
                    state = pagerState,
                    onCurrentPageChanged = {
                        onPlayTrackAtPosition(it)
                    }
                ) {
                    PlayerScreenBottomSheet(
                        onCollapsedItemClicked = {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        },
                        track = it,
                        isSelected = currentSelectedTrack.value == it,
                        onPlayPauseToggle = onPlayPauseToggle,
                        playerState = playerState.value,
                        playbackState = playbackState.value,
                        onSeekToPosition = onSeekToPosition,
                        toggleTrackToFavourite = toggleTrackToFavourite,
                        onNextClicked = onNextClicked,
                        onPreviousClicked = onPreviousClicked
                    )
                }
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