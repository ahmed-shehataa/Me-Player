package com.ashehata.me_player.util.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.util.UUID

fun Context.uriToFile(uri: Uri) = with(contentResolver) {
    val data = readUriBytes(uri) ?: return@with null
    val extension = getUriExtension(uri)
    File(
        cacheDir.path,
        "${UUID.randomUUID()}.$extension"
    ).also { audio -> audio.writeBytes(data) }
}

fun ContentResolver.readUriBytes(uri: Uri) = openInputStream(uri)
    ?.buffered()?.use { it.readBytes() }

fun ContentResolver.getUriExtension(uri: Uri) = MimeTypeMap.getSingleton()
    .getMimeTypeFromExtension(getType(uri))