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
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.modules.home.presentation.model.TracksScreenMode
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> PaginatedHorizontalPager(
    composePagingSource: ComposePagingSource<T>,
    state: PagerState = rememberPagerState(),
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
    onCurrentPageChanged: (Int) -> Unit = {},
    item: @Composable (T) -> Unit,
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
            onCurrentPageChanged(state.currentPage)
        }
    }

    HorizontalPager(
        pageCount = pagesCount.value,
        state = state,
        contentPadding = contentPadding
    ) {
        val track = composePagingSource.list[it]
        item(track)
    }

    /*Box(Modifier.fillMaxSize()) {
        if (pagingState == PagingState.LOADING_FIRST_PAGE) {
            LoadingCompose()
        } else if (pagingState == PagingState.FAILURE_AT_FIRST) {
            errorPlaceHolder()
        } else if (composePagingSource.list.isEmpty() && pagingState == PagingState.REACHED_LAST_PAGE) {
            emptyPlaceHolder()
        } else {
        }
    }*/

}