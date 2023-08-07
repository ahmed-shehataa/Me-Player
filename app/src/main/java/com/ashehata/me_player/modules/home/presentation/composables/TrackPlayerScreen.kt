package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TrackPlayerScreen(
    onCollapsedItemClicked: () -> Unit,
    onWholeItemClicked: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        ControllerItem(Modifier.align(Alignment.Center), onWholeItemClicked)
        CollapsedItem(Modifier.align(Alignment.TopCenter), onCollapsedItemClicked)

    }

}

@Composable
fun ControllerItem(modifier: Modifier, onItemClicked: () -> Unit) {
    Row(
        modifier = modifier
            .clickable {
                onItemClicked()
            }
            .background(MaterialTheme.colors.secondary)
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { },
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )

        }
        IconButton(
            onClick = { },
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )

        }
        IconButton(
            onClick = { },
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )

        }
    }

}

@Composable
fun CollapsedItem(modifier: Modifier, onItemClicked: () -> Unit) {
    Column {

        LinearProgressIndicator(
            progress = 0.7f, color = Color.Red, modifier = Modifier
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

            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)

            Text(
                modifier = Modifier.weight(1f),
                text = "trackDomainModel.name",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onSurface,
                )
            )


            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    tint = Color.Red
                )

            }

        }

    }
}