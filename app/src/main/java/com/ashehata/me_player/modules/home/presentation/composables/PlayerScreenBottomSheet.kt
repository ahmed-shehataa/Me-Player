package com.ashehata.me_player.modules.home.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import com.ashehata.me_player.common.presentation.compose.currentFraction
import com.ashehata.me_player.modules.home.presentation.model.TrackUIModel
import com.ashehata.me_player.player.PlaybackState
import com.ashehata.me_player.player.PlayerStates
import kotlinx.coroutines.flow.MutableStateFlow

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
            toggleTrackToFavourite,
            track,
        )

        WaveItem(
            Modifier.align(Alignment.BottomCenter),
            track,
            currentProgress,
            onSeekToPosition,
            playbackState.currentTrackDuration
        )

    }

}