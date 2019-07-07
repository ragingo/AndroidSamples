package com.ragingo.sample.tcpclient

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

typealias CommentReceiveCallback = (String) -> Unit

class Commentary(private val channelId: String) : XmlSocketListener {

    private companion object {
        val TAG = "Commentary"
        const val API_URL = "http://jk.nicovideo.jp/api/v2/getflv"
        const val DEFAULT_CONNECTION_TIMEOUT = 3000
        const val DEFAULT_READ_TIMEOUT = 3000

        private fun createInitialPacket(threadId: String): String {
            return "<thread thread='$threadId' version='20061206' res_from='0' scores='1' />"
        }

        private fun createConnection(url: String): URLConnection {
            val url = URL(url)
            val conn = url.openConnection()
            conn.connectTimeout = DEFAULT_CONNECTION_TIMEOUT
            conn.readTimeout = DEFAULT_READ_TIMEOUT
            return conn
        }

        suspend fun getInfo(channelId: String): GetFlvInfo =
            withContext(Dispatchers.Default) {
                val conn = createConnection("$API_URL?v=$channelId")
                conn.getInputStream().use {
                    val body = it.bufferedReader().readText()
                    return@withContext GetFlvInfo.parse(body)
                }
            }
    }

    private var commentReceiveCallback: CommentReceiveCallback? = null
    private var getFlvInfo: GetFlvInfo? = null
    private var sock: XmlSocketClient? = null

    suspend fun subscribe(callback: CommentReceiveCallback) {
        commentReceiveCallback = callback
        getFlvInfo = getInfo(channelId)

        sock = XmlSocketClient(getFlvInfo!!.msHost, getFlvInfo!!.msPort, this@Commentary)
        sock!!.start()
    }

    fun dispose() {
        if (sock == null) {
            return
        }
        sock!!.end()
        sock = null
    }

    override fun onConnected() {
        if (getFlvInfo == null) {
            return
        }
        sock!!.send(createInitialPacket(getFlvInfo!!.threadId))
    }

    override fun onReceived(msg: String) {
        Log.d(TAG, msg)

        if (!ChatInfo.isValid(msg)) {
            Log.d(TAG, "invalid data")
            return
        }

        val chat = ChatInfo.parse(msg)
        if (commentReceiveCallback != null) {
            commentReceiveCallback!!(chat.comment)
        }
    }

    override fun onError(e: Exception) {
        e.printStackTrace()
    }

    override fun onDisconnected() {
    }
}
