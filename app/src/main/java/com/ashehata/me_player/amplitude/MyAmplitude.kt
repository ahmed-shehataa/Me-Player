package com.ashehata.me_player.amplitude

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.ashehata.me_player.util.extensions.uriToFile
import dagger.hilt.android.qualifiers.ApplicationContext
import linc.com.amplituda.Amplituda
import linc.com.amplituda.AmplitudaResult
import linc.com.amplituda.exceptions.AmplitudaException
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Singleton
class MyAmplitude @Inject constructor(@ApplicationContext private val context: Context) {

    private var amplituda: Amplituda = Amplituda(context)

    suspend fun audioToWave(uri: String): List<Int> {

        return suspendCoroutine { continuation ->

            val audioFile = context.uriToFile(uri.toUri())

            amplituda.processAudio(audioFile)[{ result: AmplitudaResult<File?> ->
                val amplitudesData: List<Int> = result.amplitudesAsList()
                continuation.resume(amplitudesData)
                Log.i("audioToWave: ", "$uri: $amplitudesData")

            }, { exception: AmplitudaException? ->
                exception?.let {
                    Log.i("audioToWave: ", "exception" + exception.localizedMessage)
                    continuation.resumeWithException(exception)
                }
            }]
        }
    }

}