package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.util.extensions.toTimeFormat


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedItem(
    modifier: Modifier, onHideBottomSheet: () -> Unit,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
    track: TrackUIModel?
) {

    Row(
        modifier = modifier
            .background(MaterialTheme.colors.secondary)
            .fillMaxWidth()
            .padding(all = 20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        track?.let {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {

                val duration = remember { track.duration.toTimeFormat() }

                Text(
                    modifier = Modifier.basicMarquee(),
                    text = track.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onSurface,
                    )
                )

                Text(
                    text = duration,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.body2.copy(
                        color = MaterialTheme.colors.onPrimary,
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSurface),
                    onClick = {
                        onHideBottomSheet()
                    }) {
                    Icon(
                        modifier = Modifier.size(34.dp),
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )

                }

                IconButton(modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSurface),
                    onClick = {
                        toggleTrackToFavourite(track)
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = if (track.isFav) Color.Red else Color.Black
                    )

                }
            }


        }

    }
}
