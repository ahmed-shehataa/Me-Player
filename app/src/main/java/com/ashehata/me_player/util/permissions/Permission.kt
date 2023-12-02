package com.ashehata.me_player.util.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

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

fun Context.isPermissionGranted(permission: String, minSdk: Int = 21): Boolean {
    if (Build.VERSION.SDK_INT < minSdk) return true
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}