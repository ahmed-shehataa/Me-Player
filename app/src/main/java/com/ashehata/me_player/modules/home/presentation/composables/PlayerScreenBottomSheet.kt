package com.ashehata.me_player.modules.home.presentation.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R
import com.ashehata.me_player.common.presentation.compose.currentFraction
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.player.PlaybackState
import com.ashehata.me_player.player.PlayerStates
import com.linc.audiowaveform.AudioWaveform
import com.linc.audiowaveform.model.AmplitudeType
import com.linc.audiowaveform.model.WaveformAlignment

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerScreenBottomSheet(
    onCollapsedItemClicked: () -> Unit,
    track: TrackUIModel?,
    isSelected: Boolean,
    onPlayPauseToggle: () -> Unit,
    playerState: PlayerStates,
    playbackState: PlaybackState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    shape: Shape,
    toggleTrackToFavourite: (TrackUIModel) -> Unit,
    onSeekToPosition: (Long) -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onHideBottomSheet: () -> Unit,
) {

    val collapsedItemAlpha = remember(bottomSheetScaffoldState.currentFraction) {
        1 - bottomSheetScaffoldState.currentFraction
    }

    val expandedItemAlpha = remember(bottomSheetScaffoldState.currentFraction) {
        bottomSheetScaffoldState.currentFraction
    }

    Box(modifier = Modifier.fillMaxSize().clip(shape)) {

        val currentProgress = remember(playbackState) {
            if (playbackState.currentTrackDuration != 0L) {
                playbackState.currentPlaybackPosition.toFloat() / playbackState.currentTrackDuration.toFloat()
            } else 0F
        }

        ControllerItem(
            Modifier.align(Alignment.Center),
            playerState,
            onPlayPauseToggle,
            onNextClicked,
            onPreviousClicked,
        )


        CollapsedItem(
            Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    alpha = collapsedItemAlpha
                },
            onCollapsedItemClicked,
            track,
            onPlayPauseToggle,
            playerState,
            currentProgress,
            toggleTrackToFavourite,
            isSelected
        )

        ExpandedItem(
            Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    alpha = expandedItemAlpha
                },
            onHideBottomSheet,
            track,
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