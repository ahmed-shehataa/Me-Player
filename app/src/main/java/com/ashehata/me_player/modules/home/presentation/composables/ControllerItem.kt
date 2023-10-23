package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R
import com.ashehata.me_player.player.PlayerStates

@Composable
fun ControllerItem(
    modifier: Modifier,
    playerState: PlayerStates,
    onPlayPauseToggle: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
) {
    Box(modifier = modifier
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            onPlayPauseToggle()
        }
        .background(MaterialTheme.colors.secondary)
        .fillMaxSize()
        .padding(horizontal = 20.dp)) {

        AnimatedVisibility(
            visible = (playerState is PlayerStates.Playing).not(), Modifier.fillMaxSize(),
            enter = scaleIn(),
            exit = scaleOut()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onPreviousClicked() },
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )

                }
                IconButton(
                    onClick = { onPlayPauseToggle() },
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                ) {
                    Icon(
                        modifier = Modifier.size(34.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_play_arrow),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )

                }
                IconButton(
                    onClick = { onNextClicked() },
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )

                }
            }

        }
    }


}
