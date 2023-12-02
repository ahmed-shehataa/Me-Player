package com.ashehata.me_player.features.notification

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.ashehata.me_player.R
import com.ashehata.me_player.common.presentation.compose.PermissionScreen
import com.ashehata.me_player.common.presentation.compose.PermissionScreenModel
import com.ashehata.me_player.navigation.HomeNavigation
import com.ashehata.me_player.util.permissions.isPermissionGranted
import com.ashehata.me_player.util.permissions.requestPermissionsUtil

@Composable
fun NotificationScreen(navController: NavController) {

    val context = LocalContext.current as FragmentActivity

    val permissionScreenModel = remember {
        PermissionScreenModel(
            titleRes = R.string.notification_title,
            iconRes = R.drawable.ic_notifications,
            onRequest = {
                val toScreen =
                    if (context.isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO)) HomeNavigation.Home
                    else HomeNavigation.Media

                val navOptions = navOptions { popUpTo(0) }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.requestPermissionsUtil(
                        permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        onGranted = {
                            navController.navigate(toScreen.route, navOptions)
                        })
                } else {
                    navController.navigate(toScreen.route, navOptions)
                }
            }
        )
    }

    PermissionScreen(permissionScreenModel)
}