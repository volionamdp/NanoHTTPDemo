package com.example.nanohttpdemo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_http.*
import java.lang.String
import java.nio.charset.Charset
import java.util.*


class HttpActivity : AppCompatActivity() {

    private var webServer: WebServer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http)
        val data = applicationContext
            .assets
            .open("index.html")
            .readBytes()
            .toString(Charset.defaultCharset())

        webServer = WebServer(8080, data)
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
                ipAddressTV.text = "Please connect on this network $ip"

            } catch (e: Exception) {
                ipAddressTV.text = "Something went wrong"
                e.printStackTrace()
            }
        } else {
            ipAddressTV.text = "Connect your device with wifi"
        }
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