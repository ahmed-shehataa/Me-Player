package com.ashehata.me_player.external_audios

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.ashehata.me_player.features.home.domain.model.TrackDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun ContentResolver.readAllMediaAudio(): List<TrackDomainModel> = withContext(Dispatchers.IO) {
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

        val query = this@readAllMediaAudio.query(
            collection,
            projection,
            selection,
            null,
            null
        )

        return@withContext query?.use { cursor ->
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

            query.close()
            tracksList
        } ?: emptyList<TrackDomainModel>()
    }