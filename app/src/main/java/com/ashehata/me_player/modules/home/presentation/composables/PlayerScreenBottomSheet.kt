package com.ashehata.me_player.modules.home.presentation.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.player.PlaybackState
import com.linc.audiowaveform.AudioWaveform
import com.linc.audiowaveform.model.AmplitudeType
import com.linc.audiowaveform.model.WaveformAlignment

@Composable
fun PlayerScreenBottomSheet(
    onCollapsedItemClicked: () -> Unit,
    currentSelectedTrack: TrackUIModel?,
    onPlayPauseToggle: () -> Unit,
    isPlaying: Boolean,
    playbackState: PlaybackState,
    onSeekToPosition: (Long) -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {

        val currentProgress = remember(playbackState) {
            if (playbackState.currentTrackDuration != 0L) {
                playbackState.currentPlaybackPosition.toFloat() / playbackState.currentTrackDuration.toFloat()
            } else 0F
        }

        ControllerItem(
            Modifier.align(Alignment.Center),
            onPlayPauseToggle,
            isPlaying
        )

        CollapsedItem(
            Modifier.align(Alignment.TopCenter),
            onCollapsedItemClicked,
            currentSelectedTrack,
            onPlayPauseToggle,
            isPlaying,
            currentProgress
        )


        /*WaveItem(
            Modifier.align(Alignment.BottomCenter),
            currentSelectedTrack,
            currentProgress,
            onSeekToPosition,
            playbackState.currentTrackDuration
        )*/

    }

}

@Composable
fun WaveItem(
    modifier: Modifier,
    currentSelectedTrack: TrackDomainModel?,
    currentProgress: Float,
    onSeekToPosition: (Long) -> Unit,
    currentTrackDuration: Long
) {

    val seekToValue: (Float) -> Long = remember(currentTrackDuration) {
        {

            (String.format("%.1f", it).toDouble() * currentTrackDuration).toLong()
        }
    }

    AudioWaveform(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        style = Fill,
        waveformAlignment = WaveformAlignment.Center,
        amplitudeType = AmplitudeType.Avg,
        progressBrush = SolidColor(Color.Red),
        waveformBrush = SolidColor(MaterialTheme.colors.onSurface),
        spikeWidth = 2.dp,
        spikePadding = 2.dp,
        spikeRadius = 2.dp,
        progress = currentProgress,
        amplitudes = currentSelectedTrack?.wavesList ?: emptyList(),
        onProgressChange = {
            onSeekToPosition(seekToValue(it))
            Log.i("onSeekToPosition: ", it.toString())
        },
        onProgressChangeFinished = {},
    )
    Log.i("WaveItem: ", currentSelectedTrack?.wavesList.toString())
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ControllerItem(
    modifier: Modifier,
    onPlayPauseToggle: () -> Unit,
    isPlaying: Boolean
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
            visible = isPlaying.not(), Modifier.fillMaxSize(),
            enter = scaleIn(),
            exit = scaleOut()
        ) {

            Row(
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
                    onClick = { },
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsedItem(
    modifier: Modifier,
    onItemClicked: () -> Unit,
    currentSelectedTrack: TrackUIModel?,
    onPlayPauseToggle: () -> Unit,
    isPlaying: Boolean,
    currentProgress: Float,
) {

    Column {

        val iconRes = remember(isPlaying) {
            derivedStateOf {
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
            }
        }

        LinearProgressIndicator(
            progress = currentProgress, color = Color.Red, modifier = Modifier
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
                modifier = Modifier.weight(1f).basicMarquee(),
                text = currentSelectedTrack?.name ?: "",
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