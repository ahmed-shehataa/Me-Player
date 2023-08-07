package com.ashehata.me_player.modules.home.presentation

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.presentation.contract.TracksEvent
import com.ashehata.me_player.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val tracksViewModel: TracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // TODO Permission requests
            RequestNotificationPermission()
            RequestMediaPermission()

            AppTheme {
                TracksScreen(tracksViewModel)
            }

        }
    }

    private fun readAllMediaAudio() {
        val tracksList = mutableListOf<TrackDomainModel>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " == 1"

        val query = contentResolver.query(
            collection,
            projection,
            selection,
            null,
            null
        )

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                tracksList += TrackDomainModel(
                    id = id,
                    name = name,
                    uri = contentUri.toString(),
                    duration = duration,
                    size = size
                )
            }

            // insert all tracks into local DB
            tracksViewModel.setEvent(TracksEvent.UpdateTracks(tracksList))
            Log.i("readAllMediaAudio", tracksList.toString())
            query.close()
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
                readAllMediaAudio()
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
            } else {
                readAllMediaAudio()
            }
        }

    }
}