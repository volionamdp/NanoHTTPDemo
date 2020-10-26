package com.example.nanohttpdemo

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.File

class WebServer(
    portNumber: Int,
    private val data: String
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
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, "Not Found")
        }
    }
}