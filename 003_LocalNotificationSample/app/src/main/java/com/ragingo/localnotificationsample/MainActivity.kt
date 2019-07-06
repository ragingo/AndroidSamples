package com.ragingo.localnotificationsample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(CHANNEL_ID_1)
        }

        notification_send_button.setOnClickListener {
            sendNotification(CHANNEL_ID_1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String) {
        if (notificationManager!!.getNotificationChannel(channelId) != null) {
            if (!BuildConfig.DEBUG) {
                return
            }
            notificationManager!!.deleteNotificationChannel(channelId)
        }

        val soundUri = Uri.parse(getResourcePath("raw", DEFAULT_SOUND))
        val audioAttr =
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

        val channel = NotificationChannel(channelId, "test", NotificationManager.IMPORTANCE_DEFAULT)
        channel.setSound(soundUri, audioAttr)
        channel.enableLights(true)
        channel.lightColor = Color.parseColor("#55ddff")
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.setShowBadge(true)
        notificationManager!!.createNotificationChannel(channel)
    }

    private fun sendNotification(channelId: String) {
        val soundUri = Uri.parse(getResourcePath("raw", DEFAULT_SOUND))

        val builder  = NotificationCompat.Builder(this, channelId)
        builder.apply {
            priority = NotificationCompat.PRIORITY_DEFAULT
            setLocalOnly(true)
            setSmallIcon(R.drawable.ic_stat_name)
            setContentTitle("タイトル")
            setContentText("説明文")
            setColorized(true)
            color = Color.parseColor("#77ff77")
            setDefaults(Notification.DEFAULT_LIGHTS and Notification.DEFAULT_VIBRATE)
            setLights(Color.parseColor("#5577ff"), 100, 100)
            setVibrate(longArrayOf(0))
            setVisibility(Notification.VISIBILITY_PUBLIC)
            setSound(soundUri)
            setAutoCancel(true)
//            setContentIntent()
//            setFullScreenIntent()
        }

        GlobalScope.launch {
            async {
                downloadImage(DEFAULT_IMAGE_URL)
            }.await().let {
                if (it != null) {
                    val picStyle = NotificationCompat.BigPictureStyle(builder)
                    picStyle.bigPicture(it)
                }
                NotificationManagerCompat.from(this@MainActivity).notify(0, builder.build())
            }
        }
    }

    private fun downloadImage(url: String) : Bitmap? {
        var imageUrl: URL?
        try {
            imageUrl = URL(url)
        }
        catch (e:MalformedURLException) {
            e.printStackTrace()
            return null
        }

        var conn : URLConnection?
        try {
            conn = imageUrl.openConnection()
        }
        catch (e:IOException) {
            e.printStackTrace()
            return null
        }

        conn.connectTimeout = 3000
        conn.readTimeout = 3000

        try {
            conn.connect()
        }
        catch (e:IOException) {
            e.printStackTrace()
            return null
        }

        var bmp: Bitmap? = null
        conn.getInputStream().use {
            try {
                bmp = createBitmap(it)
            }
            catch (e:IOException) {
                e.printStackTrace()
                return null
            }
        }

        return bmp
    }

    private fun createBitmap(inputStream: InputStream) : Bitmap {
        val windowManager: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        val opts = BitmapFactory.Options()
        val bmp = BitmapFactory.decodeStream(inputStream, null, opts)
        val size = Size(opts.outWidth, opts.outHeight)
        val base = Bitmap.createBitmap(MAX_WIDTH_DP, MAX_HEIGHT_DP, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(base)

        val paint = Paint()
        paint.isFilterBitmap = true

        // 配置のデバッグ用
        paint.color = Color.BLACK
        canvas.drawRect(0.0f, 0.0f, MAX_WIDTH_DP.toFloat(), MAX_HEIGHT_DP.toFloat(), paint)
        // 幅のデバッグ用
        paint.color = Color.RED
        canvas.drawRect(MAX_WIDTH_DP - 20.0f, 0.0f, MAX_WIDTH_DP.toFloat(), 20.0f, paint)

        val x = MAX_WIDTH_DP / 2.0f
        val y = MAX_HEIGHT_DP / 2.0f

        if (size.height > MAX_HEIGHT_DP) {
            canvas.scale(MAX_HEIGHT_DP / size.width.toFloat(), MAX_HEIGHT_DP / size.height.toFloat())
            canvas.drawBitmap(bmp, x * MAX_HEIGHT_DP.toFloat() / x, 0.0f, paint)
        }
        else {
            canvas.drawBitmap(bmp, x - size.width.toFloat() / 2.0f, y - size.height / 2.0f, paint)
        }

        return base
    }

    private fun getResourcePath(resDir: String, resName: String): String {
        return String.format("%s://%s/%s/%s", ContentResolver.SCHEME_ANDROID_RESOURCE, applicationContext.packageName, resDir, resName)
    }

    companion object {
        const val CHANNEL_ID_1 = "channel_001"

        // こちらの無料素材をお借りした
        // https://soundeffect-lab.info/sound/battle/
        const val DEFAULT_SOUND = "swordslash2"

        // いらすとやからお借りした
        const val DEFAULT_IMAGE_URL = "https://1.bp.blogspot.com/-_CVATibRMZQ/XQjt4fzUmjI/AAAAAAABTNY/nprVPKTfsHcihF4py1KrLfIqioNc_c41gCLcBGAs/s800/animal_chara_smartphone_penguin.png"

        // カスタム通知領域の最大の幅(dp)
        const val MAX_WIDTH_DP = 450

        // カスタム通知領域の最大の高さ(dp)(全体 256 - タイトル 64)
        const val MAX_HEIGHT_DP = 192
    }
}
