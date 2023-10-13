package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import com.ashehata.me_player.modules.home.presentation.pagination.AllTracksPagingCompose
import com.ashehata.me_player.modules.home.presentation.pagination.FavTracksPagingCompose
import com.ashehata.me_player.modules.home.presentation.pagination.MostPlayedTracksPagingCompose
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun TracksScreenContent(
    allTracksPagingData: AllTracksPagingCompose,
    favouriteTracksPagingData: FavTracksPagingCompose,
    mostPlayedTracksPagingData: MostPlayedTracksPagingCompose,
    onTrackClicked: (TrackUIModel, Int) -> Unit,
    currentSelectedTrack: TrackUIModel?,
    screenMode: TracksScreenMode,
    onChangeScreenMode: (TracksScreenMode) -> Unit,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
    bottomPadding: Dp
) {
    val scope = rememberCoroutineScope()
    val allTracksListState = rememberLazyListState()
    val favouriteTracksListState = rememberLazyListState()
    val mostPlayedTracksListState = rememberLazyListState()
    val pagerState = rememberPagerState()
    val isScrollVisible: State<Boolean> = remember(pagerState) {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> {
                    allTracksListState.firstVisibleItemIndex > 1
                }

                1 -> {
                    favouriteTracksListState.firstVisibleItemIndex > 1
                }

                2 -> {
                    mostPlayedTracksListState.firstVisibleItemIndex > 1
                }

                else -> false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(bottom = bottomPadding)
    ) {

        Column {
            val tabs = TracksScreenMode.values()

            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {

                })

            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, type ->
                    Tab(text = { Text(stringResource(id = type.titleRes)) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            onChangeScreenMode(type)
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                                scrollToTop(
                                    screenMode,
                                    allTracksListState,
                                    favouriteTracksListState,
                                    mostPlayedTracksListState
                                )
                            }
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(26.dp),
                                imageVector = ImageVector.vectorResource(id = type.iconRes),
                                contentDescription = null
                            )
                        }
                    )
                }
            }


            HorizontalPager(pageCount = TracksScreenMode.values().size, state = pagerState) {
                when (it) {
                    0 -> {
                        TracksList(
                            tracksPagingData = allTracksPagingData,
                            listState = allTracksListState,
                            currentSelectedTrack = currentSelectedTrack,
                            onTrackClicked = onTrackClicked,
                            toggleTrackToFavourite = toggleTrackToFavourite
                        )
                    }

                    1 -> {
                        TracksList(
                            tracksPagingData = favouriteTracksPagingData,
                            listState = favouriteTracksListState,
                            currentSelectedTrack = currentSelectedTrack,
                            onTrackClicked = onTrackClicked,
                            toggleTrackToFavourite = toggleTrackToFavourite

                        )
                    }

                    2 -> {
                        TracksList(
                            tracksPagingData = mostPlayedTracksPagingData,
                            listState = mostPlayedTracksListState,
                            currentSelectedTrack = currentSelectedTrack,
                            onTrackClicked = onTrackClicked,
                            toggleTrackToFavourite = toggleTrackToFavourite

                        )
                    }
                }
            }


        }

        AnimatedVisibility(
            visible = isScrollVisible.value, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            ScrollTopButton(Modifier) {
                scope.launch {
                    scrollToTop(
                        screenMode,
                        allTracksListState,
                        favouriteTracksListState,
                        mostPlayedTracksListState
                    )
                }
            }
        }

    }

}

suspend fun scrollToTop(
    screenMode: TracksScreenMode,
    allTracksListState: LazyListState,
    favouriteTracksListState: LazyListState,
    mostPlayedTracksListState: LazyListState
) {

    when (screenMode) {
        TracksScreenMode.All -> {
            allTracksListState.animateScrollToItem(0)
        }

        TracksScreenMode.Favourite -> {
            favouriteTracksListState.animateScrollToItem(0)
        }

        TracksScreenMode.MostPlayed -> {
            mostPlayedTracksListState.animateScrollToItem(0)
        }
    }
}