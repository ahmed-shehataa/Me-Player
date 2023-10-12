package com.ashehata.me_player.common.presentation.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.base.PagingState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> PaginatedLazyColumn(
    composePagingSource: ComposePagingSource<T>,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
    item: @Composable (T) -> Unit,
    emptyPlaceHolder: @Composable () -> Unit = {
        EmptyListPlaceholder(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    },
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
) {

    val onRefreshingData = remember {
        {
            onRefresh()
            composePagingSource.refresh()
        }
    }

    val refreshState = rememberPullRefreshState(isRefreshing, onRefreshingData)

    val currentVisibleItem =
        remember { derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index } }

    val pagingState = composePagingSource.state.collectAsState().value

    LaunchedEffect(key1 = currentVisibleItem.value) {
        // if reached to the end of current page, so load next one
        val index = currentVisibleItem.value ?: 0
        val itemIndexToLoad = lazyListState.layoutInfo.totalItemsCount - 4

        if (itemIndexToLoad > 0)
            if (index > (lazyListState.layoutInfo.totalItemsCount - 4)) {
                composePagingSource.loadNextPage()
            }
    }

    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {

        if (pagingState == PagingState.LOADING_FIRST_PAGE) {
            LoadingCompose()
        } else if (pagingState == PagingState.FAILURE_AT_FIRST) {
            errorPlaceHolder()
        } else if (composePagingSource.list.isEmpty() && pagingState == PagingState.REACHED_LAST_PAGE) {
            emptyPlaceHolder()
        } else {
            LazyColumn(
                state = lazyListState,
                contentPadding = contentPadding,
                modifier = Modifier.animateContentSize()
            ) {

                items(composePagingSource.list) {
                    item(it)
                }

                item {
                    if (pagingState == PagingState.LOADING_NEXT_PAGE) {
                        loadingPlaceHolder()
                    } else if (pagingState == PagingState.FAILURE_AT_NEXT) {
                        ErrorPlaceholder()
                    }
                }
            }
        }

        PullRefreshIndicator(
            isRefreshing,
            refreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }

}