package com.ragingo.sample.tcpclient

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.*

interface XmlSocketListener {
    fun onConnected()
    fun onReceived(msg: String)
    fun onError(e: Exception)
    fun onDisconnected()
}

class XmlSocketClient(private val host: String, private val port: Int, private val listener: XmlSocketListener) {
    private var selector: Selector? = null
    private var channel: SocketChannel? = null
    private var receivedBuffer: ArrayList<Byte> = ArrayList()
    private val syncObj = Any()

    private companion object {
        const val CONNECTION_TIMEOUT = 10 * 1000 // 接続タイムアウト
        const val RECEIVE_BUFFER_SIZE = 1024

        private fun writeDebugLog(msg: String) {
            Log.d(XmlSocketClient.javaClass.simpleName, msg)
        }
    }

    fun start() {
        writeDebugLog("start")
        Thread {
            startCore()
        }.start()
    }

    private fun startCore() {
        writeDebugLog("startCore")
        try {
            selector = Selector.open()
            channel = SocketChannel.open()
            channel!!.socket().soTimeout = CONNECTION_TIMEOUT
            channel!!.connect(InetSocketAddress(host, port))
            channel!!.configureBlocking(false)
        } catch (e: IOException) {
            onError(e)
            return
        }

        onConnected()

        try {
            channel!!.register(selector, SelectionKey.OP_READ)
        } catch (e: ClosedChannelException) {
            onError(e)
            return
        }

        while (selector!!.select() > 0) {
            val it = selector!!.selectedKeys().iterator()
            while (it.hasNext()) {
                val key = it.next()
                it.remove()

                if (!channel!!.isConnected) {
                    return
                }

                if (!key.isReadable) {
                    continue
                }

                val buf = ByteBuffer.allocate(RECEIVE_BUFFER_SIZE)

                if (channel!!.read(buf) < 0) {
                    continue
                }
                buf.flip()

                (0 until buf.limit()).forEach { i ->
                    // 区切りの場所でリスナーに渡す
                    if (buf.get(i).toInt() == 0) {
                        val msg = receivedBuffer.toByteArray().toString(Charsets.UTF_8)
                        onReceived(msg)
                        receivedBuffer.clear()
                    } else {
                        receivedBuffer.add(buf.get(i))
                    }
                }
            }

        }
    }

    fun end() {
        writeDebugLog("end")
        try {
            synchronized(syncObj) {
                channel!!.close()
                selector!!.close()
            }
            onDisconnected()
        } catch (e: IOException) {
            onError(e)
        }
    }

    fun send(msg: String) {
        writeDebugLog("send")
        Thread {
            sendCore(msg)
        }.start()
    }

    private fun sendCore(msg: String) {
        writeDebugLog("送信開始")
        writeDebugLog(msg)

        if (channel == null) {
            writeDebugLog("準備未完了なので送信できません")
            return
        }

        if (!channel!!.isConnected) {
            writeDebugLog("接続されていないので送信できません")
            return
        }

        try {
            synchronized(syncObj) {
                val stream = ByteArrayOutputStream()
                stream.write(msg.toByteArray())
                stream.write(0)
                channel!!.write(ByteBuffer.wrap(stream.toByteArray()))
            }
        } catch (e: IOException) {
            onError(e)
        }

    }

    private fun onConnected() {
        writeDebugLog("onConnected")
        MainThreadHandler().post {
            listener.onConnected()
        }
    }

    private fun onReceived(msg: String) {
        writeDebugLog("onReceived")
        MainThreadHandler().post {
            listener.onReceived(msg)
        }
    }

    private fun onError(e: Exception) {
        writeDebugLog("onError")
        MainThreadHandler().post {
            listener.onError(e)
        }
    }

    private fun onDisconnected() {
        writeDebugLog("onDisconnected")
        MainThreadHandler().post {
            listener.onDisconnected()
        }
    }

}

class MainThreadHandler : Handler(Looper.getMainLooper())
