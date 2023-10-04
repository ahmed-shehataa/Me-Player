package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.util.extensions.toTimeFormat


@Composable
fun TrackItem(
    trackUIModel: TrackUIModel,
    onTrackClicked: () -> Unit,
    isSelected: Boolean = false
) {

    val trackNameColor: @Composable () -> Color = remember(isSelected) {
        {
            if (isSelected)
                MaterialTheme.colors.secondaryVariant
            else
                MaterialTheme.colors.onSurface
        }
    }

    val iconRes: () -> Int = remember(isSelected) {
        {
            if (isSelected)
                R.drawable.ic_graphic_eq
            else
                R.drawable.ic_music_note
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTrackClicked()
            }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.secondary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                imageVector = ImageVector.vectorResource(id = iconRes()),
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colors.secondaryVariant else Color.Unspecified
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            Text(
                text = trackUIModel.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.body1.copy(
                    color = trackNameColor(),
                )
            )

            Text(
                text = trackUIModel.duration.toTimeFormat(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.onPrimary,
                )
            )
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null, tint = Color.Red)

        }

    }
}