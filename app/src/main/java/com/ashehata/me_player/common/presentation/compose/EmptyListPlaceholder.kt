package com.ashehata.me_player.common.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R


@Composable
fun EmptyListPlaceholder(modifier: Modifier = Modifier) {

    Box(
        modifier
            .fillMaxSize()
            .testTag("EmptyListPlaceholder")) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.size(150.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_graphic_eq),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White)
            )

            Text(
                text = stringResource(id = R.string.no_tracks_found),
                style = MaterialTheme.typography.body1.copy(
                    color = Color.White
                )
            )

        }
    }
}