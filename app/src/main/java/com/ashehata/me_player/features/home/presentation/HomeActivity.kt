package com.ashehata.me_player.features.home.presentation

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ashehata.me_player.features.home.presentation.composables.TracksScreen
import com.ashehata.me_player.features.home.presentation.contract.TracksEvent
import com.ashehata.me_player.features.media.MediaScreen
import com.ashehata.me_player.features.notification.NotificationScreen
import com.ashehata.me_player.navigation.HomeNavigation
import com.ashehata.me_player.player.MyPlayer
import com.ashehata.me_player.service.PlaybackService
import com.ashehata.me_player.theme.AppTheme
import com.ashehata.me_player.util.permissions.isPermissionGranted
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    companion object {
        const val OPEN_BS_ACTION = "com.ashehata.me_player.action.BS_ACTION"
    }

    private val sessionToken by lazy {
        SessionToken(this, ComponentName(this, PlaybackService::class.java))
    }
    private val mediaController by lazy {
        MediaController.Builder(this, sessionToken)
    }

    private val tracksViewModel: TracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val startDestination: () -> HomeNavigation = remember { { getNextDestination() } }

            AppTheme {
                NavHost(
                    navController = navController,
                    startDestination = startDestination().route
                ) {
                    composable(HomeNavigation.Notification.route) { NotificationScreen(navController) }
                    composable(HomeNavigation.Media.route) { MediaScreen(navController) }
                    composable(HomeNavigation.Home.route) { TracksScreen(tracksViewModel) }
                }
            }
        }
    }

    private fun getNextDestination(): HomeNavigation {
        return when {
            isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS, Build.VERSION_CODES.TIRAMISU)
                    && isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO) -> HomeNavigation.Home
            isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS).not() -> HomeNavigation.Notification
            isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO).not() -> HomeNavigation.Media
            else -> HomeNavigation.Notification
        }
    }


    override fun onStart() {
        super.onStart()
        mediaController.buildAsync().run {
            addListener(
                /* listener = */ { tracksViewModel.setEvent(TracksEvent.InitPlayer(MyPlayer(this.get()))) },
                /* executor = */ MoreExecutors.directExecutor()
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == OPEN_BS_ACTION) {
            tracksViewModel.setEvent(TracksEvent.OpenBottomSheet)
        }
    }

}