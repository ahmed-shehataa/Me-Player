package com.ashehata.me_player.features.home.presentation.composables

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.common.models.PaginatedItem
import com.ashehata.me_player.common.presentation.GeneralObservers
import com.ashehata.me_player.common.presentation.compose.LoadingDialog
import com.ashehata.me_player.common.presentation.compose.PaginatedHorizontalPager
import com.ashehata.me_player.common.presentation.compose.currentFraction
import com.ashehata.me_player.external_audios.readAllMediaAudio
import com.ashehata.me_player.features.home.presentation.TracksViewModel
import com.ashehata.me_player.features.home.presentation.contract.TracksEvent
import com.ashehata.me_player.features.home.presentation.contract.TracksState
import com.ashehata.me_player.features.home.presentation.contract.TracksViewState
import com.ashehata.me_player.features.home.presentation.model.TrackUIModel
import com.ashehata.me_player.features.home.presentation.model.TracksScreenMode
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TracksScreen(viewModel: TracksViewModel) {

    /**
     * States
     */
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed))

    val viewStates = remember { viewModel.viewStates ?: TracksViewState() }

    val currentSelectedTrack = remember { viewStates.currentSelectedTrack }

    val playbackState = viewStates.playbackState.collectAsState()

    val playerState = remember { viewStates.playerState }

    val screenMode = remember { viewStates.screenMode }

    val tracksBufferDialog = remember { viewStates.tracksBuffer }

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

    val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
        bottomSheetPagingSource.value.size()
    }

    val bottomSheetHeight =
        animateDpAsState(
            targetValue = if (currentSelectedTrack.value == null) 0.dp else 68.dp,
            animationSpec = tween(
                easing = EaseOutSine
            )
        )

    /**
     * Effects
     */
    LaunchedEffect(key1 = null) {
        scope.launch {
            viewModel.setEvent(TracksEvent.UpdateTracks(context.contentResolver.readAllMediaAudio()))
        }
    }

    /**
     * Actions
     */

    val onPlayTrackAtPosition: (Int, TrackUIModel, Boolean) -> Unit = remember {
        { index, track, force ->
            viewModel.setEvent(TracksEvent.PlayTrackAtPosition(index, track, force))
        }
    }

    val onTrackClicked: (TrackUIModel, Int) -> Unit = remember {
        { track, index ->
            if (currentSelectedTrack.value == track) {
                // just open bottom sheet
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            } else {
                // play current track
                Log.i("TracksScreen: track", track.toString())
                Log.i("TracksScreen: index", index.toString())
                onPlayTrackAtPosition(index, track, true)
                scope.launch { pagerState.scrollToPage(index) }
            }
        }
    }

    val collapseSheet: () -> Unit = {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    val expandSheet: () -> Unit = {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }

    val scrollToIndex: (Int) -> Unit = {
        scope.launch {
            pagerState.animateScrollToPage(it)
        }
    }

    val toggleTrackToFavourite: (TrackUIModel) -> Unit = remember {
        {
            viewModel.setEvent(TracksEvent.ToggleTrackToFavourite(it))
        }
    }

    val onNextClicked: () -> Unit = remember {
        {
            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
        }
    }

    val onPreviousClicked: () -> Unit = remember {
        {
            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
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

    LaunchedEffect(key1 = currentSelectedTrack, block = {
        scope.launch { pagerState.scrollToPage(12) }
    })

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    val bottomSheetShape = remember(bottomSheetScaffoldState.currentFraction) {
        val radius = (16 * bottomSheetScaffoldState.currentFraction).dp
        RoundedCornerShape(topStart = radius, topEnd = radius)
    }

    /**
     * UI
     */

    BottomSheetScaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .statusBarsPadding()
            .navigationBarsPadding(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (currentSelectedTrack.value != null)
                PaginatedHorizontalPager(
                    composePagingSource = bottomSheetPagingSource.value as ComposePagingSource<PaginatedItem>,
                    state = pagerState,
                    pageSpacing = 4.dp,
                    onCurrentPageChanged = { index, track ->
                        Log.i(
                            "onCurrentPageChanged",
                            "TracksScreen: " + currentSelectedTrack.value.toString()
                        )
                        onPlayTrackAtPosition(index, track as TrackUIModel, false)
                    }
                ) {
                    Log.i(
                        "PaginatedHorizontal",
                        "TracksScreen: " + currentSelectedTrack.value.toString()
                    )
                    PlayerScreenBottomSheet(
                        onCollapsedItemClicked = {
                            expandSheet()
                        },
                        track = it as TrackUIModel,
                        isSelected = currentSelectedTrack.value == it,
                        onPlayPauseToggle = onPlayPauseToggle,
                        playerState = playerState.value,
                        playbackState = playbackState.value,
                        onSeekToPosition = onSeekToPosition,
                        toggleTrackToFavourite = toggleTrackToFavourite,
                        onNextClicked = onNextClicked,
                        onPreviousClicked = onPreviousClicked,
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        shape = bottomSheetShape,
                        onHideBottomSheet = {
                            collapseSheet()
                        }
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
            bottomPadding = 68.dp,
            sheetPadding = it.calculateBottomPadding()
        )

        if (tracksBufferDialog.value?.isBuffering == true)
            LoadingDialog(tracksBuffer = tracksBufferDialog.value!!)
    }

    GeneralObservers<TracksState, TracksViewModel>(viewModel = viewModel) {
        when (it) {
            TracksState.ExpandBottomSheet -> {
                expandSheet()
            }

            is TracksState.ScrollToIndex -> {
                scrollToIndex(it.index)
            }
        }
    }

}