package com.ashehata.me_player.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.ashehata.brightskies_task.ui.theme.Shapes

private val DarkColorPalette = lightColors(
    primary = Cod_Gray,
    primaryVariant = Cod_Gray_2,
    secondary = Tundora,
    surface = Cod_Gray_2,
    onSurface = White,
    onPrimary = Nobel
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}