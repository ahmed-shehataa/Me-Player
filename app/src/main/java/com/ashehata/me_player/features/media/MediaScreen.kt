package com.ashehata.me_player.features.media

import android.Manifest
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
import com.ashehata.me_player.util.permissions.requestPermissionsUtil

@Composable
fun MediaScreen(navController: NavController) {

    val context = LocalContext.current as FragmentActivity

    val permissionScreenModel = remember {
        PermissionScreenModel(
            titleRes = R.string.media_title,
            iconRes = R.drawable.ic_media,
            onRequest = {
                context.requestPermissionsUtil(
                    permissions = arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                    onGranted = {
                        navController.navigate(HomeNavigation.Home.route, navOptions {
                            popUpTo(0)
                        })
                    })
            }
        )
    }

    PermissionScreen(permissionScreenModel)
}