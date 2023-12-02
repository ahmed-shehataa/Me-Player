package com.ashehata.me_player.features.home.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.R


@Composable
fun ScrollTopButton(
    modifier: Modifier,
    onClicked: () -> Unit
) {

    IconButton(modifier = modifier
        .size(50.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colors.secondary), onClick = {
        onClicked()
    }) {
        Icon(
            modifier = Modifier.size(36.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_top),
            contentDescription = null,
            tint = Color.White
        )

    }

}