package com.ashehata.me_player.modules.home.presentation.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.ashehata.me_player.R
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import kotlinx.coroutines.flow.Flow


@Composable
fun TracksScreenContent(allTracksPagingData: Flow<PagingData<TrackDomainModel>>?) {

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
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            2 -> Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }

        LazyColumn(contentPadding = PaddingValues(top = 20.dp)) {


            allTracks?.let {
                Toast.makeText(context, allTracks.itemCount.toString(), Toast.LENGTH_SHORT).show()
                items(allTracks.itemCount) { index ->
                    val item = allTracks[index]
                    item?.let { it1 -> TrackItem(it1) }
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