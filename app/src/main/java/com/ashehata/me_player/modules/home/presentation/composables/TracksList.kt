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
import com.ashehata.me_player.common.presentation.compose.PaginatedLazyColumn
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel

@Composable
fun TracksList(
    tracksPagingData: ComposePagingSource<TrackUIModel>,
    currentSelectedTrack: TrackUIModel?,
    onTrackClicked: (TrackUIModel) -> Unit,
    listState: LazyListState,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
) {

    PaginatedLazyColumn(
        composePagingSource = tracksPagingData,
        lazyListState = listState,
        contentPadding = PaddingValues(vertical = 20.dp),
        item = { track ->
            TrackItem(
                trackUIModel = track,
                isSelected = track == currentSelectedTrack,
                onTrackClicked = {
                    onTrackClicked(track)
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