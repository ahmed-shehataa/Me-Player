package com.ashehata.me_player.common.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag


@Composable
fun LoadingCompose() {
    Box(modifier = Modifier
        .fillMaxSize()
        .testTag("loading_view")) {
        CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color.Red)
    }
}