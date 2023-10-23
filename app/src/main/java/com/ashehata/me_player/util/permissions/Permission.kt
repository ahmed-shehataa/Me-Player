package com.ashehata.me_player.util.permissions

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.ashehata.me_player.external_audios.readAllMediaAudio
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch

fun FragmentActivity.requestPermissionsUtil(
    permissions: Array<out String>,
    onGranted: () -> Unit
) {
    PermissionX.init(this)
        .permissions(permissions.asList())
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                "We need to access to your audio files so we can display and play them for you",
                "OK",
                "Cancel"
            )
        }
        .request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                onGranted()
            } else {
                Toast.makeText(
                    this,
                    "These permissions are denied: $deniedList",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}
