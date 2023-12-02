package com.ashehata.me_player.common.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.ashehata.me_player.R
import com.ashehata.me_player.features.home.presentation.model.TracksBuffer

@Composable
fun LoadingDialog(
    tracksBuffer: TracksBuffer,
) {

    var showAnimatedDialog by remember { mutableStateOf(false) }
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }


    Dialog(content = {
        (LocalView.current.parent as? DialogWindowProvider)?.window?.setWindowAnimations(-1)

        AnimatedVisibility(
            visible = animateIn && tracksBuffer.isBuffering,
            enter = fadeIn(spring(stiffness = Spring.StiffnessHigh)) + scaleIn(
                initialScale = .3f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(targetScale = .95f)
        ) {


            Box(
                Modifier
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.secondary),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.please_wait),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                    )

                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.tracks_buffering),
                        color = MaterialTheme.colors.onPrimary
                    )

                    LinearProgressIndicator(
                        progress = tracksBuffer.getPercentage(),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Red,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = "${tracksBuffer.buffered}/${tracksBuffer.totalCount}",
                        color = Color.White
                    )

                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    showAnimatedDialog = false
                }
            }
        }
    }, onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Preview
@Composable
private fun Preview() {
    LoadingDialog(
        tracksBuffer = TracksBuffer(isBuffering = true, totalCount = 1000, buffered = 20)
    )
}