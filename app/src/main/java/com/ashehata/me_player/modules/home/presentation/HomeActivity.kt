package com.ashehata.me_player.modules.home.presentation

import android.Manifest
import android.content.ComponentName
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.ashehata.me_player.external_audios.readAllMediaAudio
import com.ashehata.me_player.modules.home.presentation.composables.TracksScreen
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.service.PlaybackService
import com.ashehata.me_player.theme.AppTheme
import com.ashehata.me_player.util.permissions.requestPermissionsUtil
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val tracksViewModel: TracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            this.requestPermissionsUtil(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_MEDIA_AUDIO
                ),
                onGranted = {
                    lifecycleScope.launch {
                        tracksViewModel.setEvent(TracksEvent.UpdateTracks(contentResolver.readAllMediaAudio()))
                    }
                })
            AppTheme {
                TracksScreen(tracksViewModel)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                // init player
                tracksViewModel.setEvent(TracksEvent.InitPlayer(MyPlayer(controllerFuture.get())))
            },
            MoreExecutors.directExecutor()
        )

    }

}