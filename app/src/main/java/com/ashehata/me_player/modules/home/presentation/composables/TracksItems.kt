package com.ashehata.me_player.modules.home.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel

@Composable
fun TracksItems(
    tracksPagingData: LazyPagingItems<TrackUIModel>,
    currentSelectedTrack: TrackUIModel?,
    onTrackClicked: (TrackUIModel) -> Unit,
    listState: LazyListState,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
) {

    LazyColumn(contentPadding = PaddingValues(top = 20.dp), state = listState) {

        Log.i("TracksItems", "TracksItems: " + tracksPagingData.itemCount)
        items(tracksPagingData.itemCount) {
            val track = tracksPagingData[it]

            if (track != null) {
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
        }

    }

}