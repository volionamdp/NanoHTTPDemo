package com.example.nanohttpdemo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream

class WebServer(
    portNumber: Int,
    private val data: String,
    private var context: Context?
) : NanoHTTPD(portNumber) {

    override fun serve(session: IHTTPSession?): Response? {

        val uri = session?.uri
        Log.e("Wb", uri)

        return when (uri) {
            "/index" -> {
                newFixedLengthResponse(data)
            }
            "/download" -> {
                newFixedLengthResponse(Response.Status.OK, "application/octet-stream", "Hello world")
            }
            else ->{
                val inputStream  = uriFile?.let { context?.contentResolver?.openInputStream(it) }
                Log.d("zzzzet", "serve: ${inputStream?.available()}  ${getMime(context,uriFile)}")
                val file = File("/data/data/com.example.nanohttpdemo/files/test.mp4")
//                newFixedLengthResponse(Response.Status.OK,getMime(context,uriFile),inputStream,
//                    inputStream?.available()?.toLong()?:0L
//                )
                newFixedLengthResponse(Response.Status.OK,"video/mp4",FileInputStream(file),
                    file.length()
                )
            }
        }
    }
    private fun getMime(context: Context?, uri: Uri?): kotlin.String {
        val cR: ContentResolver? = context?.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val ex = mime.getExtensionFromMimeType(uri?.let { cR?.getType(it) }) ?: ""
        return mime.getMimeTypeFromExtension(ex)?:""

    }
    var uriFile:Uri? = null


}