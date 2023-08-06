package com.ashehata.me_player.modules.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Permission requests
            RequestNotificationPermission()
            RequestMediaPermission()

            Box(
                Modifier
                    .background(Color.Black)
                    .fillMaxSize()
            ) {

            }
        }
    }

    @Composable
    private fun RequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Toast.makeText(this, "Notification isGranted", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Show dialog for Notification", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(key1 = null) {
                if (
                    ContextCompat.checkSelfPermission(
                        this@HomeActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    launcher.launch(permission)
                }
            }
        }


    }

    @Composable
    private fun RequestMediaPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else Manifest.permission.READ_EXTERNAL_STORAGE


        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Media isGranted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Show dialog Media", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(key1 = null) {
            if (
                ContextCompat.checkSelfPermission(
                    this@HomeActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(permission)
            }
        }

    }
}