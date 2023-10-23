package com.ashehata.me_player.modules.home.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.linc.audiowaveform.AudioWaveform
import com.linc.audiowaveform.model.AmplitudeType
import com.linc.audiowaveform.model.WaveformAlignment

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
