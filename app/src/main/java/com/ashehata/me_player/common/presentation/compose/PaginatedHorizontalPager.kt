package com.ashehata.me_player.common.presentation.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.common.models.PaginatedItem
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaginatedHorizontalPager(
    composePagingSource: ComposePagingSource<PaginatedItem>,
    state: PagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        composePagingSource.size()
    },
    pageSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(),
    errorPlaceHolder: @Composable () -> Unit = {
        ErrorPlaceholder(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    },
    loadingPlaceHolder: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            CircularProgressIndicator(
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    },
    onCurrentPageChanged: (Int, PaginatedItem) -> Unit = {index, item -> },
    item: @Composable (PaginatedItem) -> Unit,
) {

    val pagingState = composePagingSource.state.collectAsState().value

    val pagesCount = remember(composePagingSource.size()) {
        derivedStateOf {
            if (composePagingSource.size() == 0) 0 else composePagingSource.size()
        }
    }

    LaunchedEffect(key1 = state.currentPage) {
        // if reached to the end of current page, so load next one
        if (state.currentPage == pagesCount.value - 1) {
            composePagingSource.loadNextPage()
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.distinctUntilChanged().collect { page ->
            onCurrentPageChanged(state.currentPage, composePagingSource.list.get(state.currentPage))
        }
    }

    HorizontalPager(
        modifier = Modifier,
        state = state,
        pageSpacing = pageSpacing,
        contentPadding = contentPadding,
        pageContent = {
            val track = composePagingSource.list[it]
            item(track)
        }
    )

}