package com.example.nanohttpdemo

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.example.nanohttpdemo.server.SimpleWebServer
import fi.iki.elonen.NanoHTTPD
import kotlinx.android.synthetic.main.activity_http.*
import java.io.File
import java.lang.String
import java.nio.charset.Charset
import java.util.*


class HttpActivity : AppCompatActivity() {

    private var webServer: NanoHTTPD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http)
        Log.d("aaaa", "onCreate: ")
        val data = applicationContext
            .assets
            .open("index.html")
            .readBytes()
            .toString(Charset.defaultCharset())

        val port = 2222
//        webServer = WebServer(port,"",applicationContext)
        "/data/data/com.example.nanohttpdemo/files/audio.m3u"
        val list:MutableList<File> = mutableListOf()
//        list.add(File("/data/data/com.example.nanohttpdemo/files/"))
        list.add( Environment.getExternalStorageDirectory())
        webServer = SimpleWebServer(null,port,list,false,null)

//        webServer=Server(applicationContext,port)
        webServer?.start()

        if (isUserConnectedOnWiFi()) {
            val wm = getSystemService(WIFI_SERVICE) as WifiManager
            try {
                val ipAddress: Int = wm.connectionInfo.ipAddress
                val ip = String.format(
                    Locale.ENGLISH,
                    "%d.%d.%d.%d",
                    ipAddress and 0xff,
                    ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )
                Log.d("IP", ip)
                ipAddressTV.setText("http://$ip/$port")

            } catch (e: Exception) {
                ipAddressTV.setText( "Something went wrong")
                e.printStackTrace()
            }
        } else {
            ipAddressTV.setText("Connect your device with wifi")
        }
        findViewById<View>(R.id.ipAddressTV).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, PICKFILE_REQUEST_CODE)
        }
    }



    val PICKFILE_REQUEST_CODE = 10
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
            Log.d(
                "aaaaa",
                "onActivityResult: ${data?.data?.toString()}"
            )
            val cR: ContentResolver = application.getContentResolver()
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(cR.getType(Uri.parse(data?.data?.path)))
            val uri = data?.data?: Uri.parse("")
            val file = DocumentFile.fromSingleUri(applicationContext, uri)
            Log.d("zzz", "onActivityResult:$ "+getMimeType(applicationContext,uri)+" "+contentResolver.openInputStream(uri)?.available())
//            webServer?.uriFile = data?.data


        }
        Environment.DIRECTORY_DOWNLOADS
    }
    fun getMimeType(context: Context, uri: Uri): kotlin.String? {

        //Check uri format to avoid null
        val extension: kotlin.String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
//            val mime = MimeTypeMap.getSingleton()
//            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
            context.contentResolver.getType(uri)
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    override fun onDestroy() {
        super.onDestroy()
        webServer?.stop()
    }

    private fun isUserConnectedOnWiFi(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetwork = cm.getNetworkCapabilities(cm.activeNetwork)
        return if (actNetwork != null) {
            when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        } else {
            false
        }
    }


}