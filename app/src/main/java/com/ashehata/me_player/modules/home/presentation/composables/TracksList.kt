package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.base.ComposePagingSource
import com.ashehata.me_player.common.models.PaginatedItem
import com.ashehata.me_player.common.presentation.compose.PaginatedLazyColumn
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel

@Composable
fun TracksList(
    tracksPagingData: ComposePagingSource<TrackUIModel>,
    currentSelectedTrack: TrackUIModel?,
    onTrackClicked: (TrackUIModel, Int) -> Unit,
    listState: LazyListState,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
    contentPadding: PaddingValues,
    header: @Composable () -> Unit = {}
) {

    PaginatedLazyColumn(
        composePagingSource = tracksPagingData as ComposePagingSource<PaginatedItem>,
        lazyListState = listState,
        contentPadding = contentPadding,
        header = header,
        item = { track ->
            TrackItem(
                trackUIModel = track as TrackUIModel,
                isSelected = track == currentSelectedTrack,
                onTrackClicked = {
                    onTrackClicked(track, tracksPagingData.list.indexOf(track))
                },
                toggleTrackToFavourite = toggleTrackToFavourite
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Divider(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    )

}