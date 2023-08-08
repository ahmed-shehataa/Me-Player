package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.ashehata.me_player.R
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import kotlinx.coroutines.flow.Flow


@Composable
fun TracksScreenContent(
    allTracksPagingData: Flow<PagingData<TrackDomainModel>>?,
    onTrackClicked: (TrackDomainModel) -> Unit,
    currentSelectedTrack: TrackDomainModel?,
) {

    val context = LocalContext.current

    Column(Modifier.background(MaterialTheme.colors.primary)) {
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(R.string.all, R.string.fav, R.string.most_played)
        val allTracks = allTracksPagingData?.collectAsLazyPagingItems()

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

        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(stringResource(id = title)) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        val iconRes = when (index) {
                            0 -> R.drawable.ic_queue_music
                            1 -> R.drawable.ic_favorite
                            2 -> R.drawable.ic_headset_mic
                            else -> R.drawable.ic_queue_music
                        }
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = ImageVector.vectorResource(id = iconRes),
                            contentDescription = null
                        )
                    }
                )
            }
        }

        LazyColumn(contentPadding = PaddingValues(top = 20.dp)) {


            allTracks?.let {
                //Toast.makeText(context, allTracks.itemCount.toString(), Toast.LENGTH_SHORT).show()

                items(allTracks.itemCount) { index ->

                    allTracks[index]?.let { currentTrack ->
                        TrackItem(
                            trackDomainModel = currentTrack,
                            isSelected = currentTrack == currentSelectedTrack,
                            onTrackClicked = {
                                onTrackClicked(currentTrack)
                            }
                        )
                    }
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

}