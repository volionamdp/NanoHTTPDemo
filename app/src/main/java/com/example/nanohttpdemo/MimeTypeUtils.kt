package com.example.nanohttpdemo

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.TypedValue
import android.webkit.MimeTypeMap

object MimeTypeUtils {
    fun tryGetMediaMimetype(context: Context, uri: Uri): String? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        } catch (e: Exception) {
            e.message
        }
    }

    fun tryGetImageMimetype(resource: Resources, resId: Int): String? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        return try {
            BitmapFactory.decodeResource(resource, resId, options)
            options.outMimeType
        } catch (e: OutOfMemoryError) {
            return null
        }
    }

    fun tryGetMimeTypeFromExtension(resource: Resources, resId: Int): String? {
        val value = TypedValue()
        resource.getValue(resId, value, true)
        val fileName = value.string.toString()

        val dotIndex = fileName.lastIndexOf(".")
        val extension = if (dotIndex >= 0) {
            fileName.substring(dotIndex + 1)
        } else null

        return if (extension != null) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        } else null
    }
}