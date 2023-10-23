package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.player.PlayerStates

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsedItem(
    modifier: Modifier,
    onItemClicked: () -> Unit,
    currentSelectedTrack: TrackUIModel?,
    onPlayPauseToggle: () -> Unit,
    playerState: PlayerStates,
    currentProgress: Float,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
    isSelected: Boolean
) {

    Column {

        val progressState = remember(isSelected, currentProgress) {
            derivedStateOf { if (isSelected) currentProgress else 0f }
        }

        val iconRes = remember(playerState) {
            derivedStateOf {
                if (playerState is PlayerStates.Playing) R.drawable.ic_pause else R.drawable.ic_play_arrow
            }
        }

        LinearProgressIndicator(
            progress = progressState.value, color = Color.Red, modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            backgroundColor = Color.Unspecified
        )

        Row(
            modifier = modifier
                .clickable {
                    onItemClicked()
                }
                .background(MaterialTheme.colors.secondary)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            currentSelectedTrack?.let {
                IconButton(onClick = {
                    onPlayPauseToggle()
                }) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = ImageVector.vectorResource(id = iconRes.value),
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    text = currentSelectedTrack.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onSurface,
                    )
                )

                IconButton(onClick = {
                    toggleTrackToFavourite(currentSelectedTrack)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = if (currentSelectedTrack.isFav) Color.Red else Color.White
                    )

                }
            }

        }

    }
}