package com.ragingo.camerasample

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresPermission
import android.util.Log
import android.view.Surface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private val isCameraRunning: Boolean
        get() {
            if (camera == null) {
                return false
            }
            if (camera!!.state != Camera.State.Open &&
                camera!!.state != Camera.State.Capture) {
                return false
            }
            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_preview_button.setOnClickListener {
            changeCameraState()
        }
    }

    private fun changeCameraState() {
        if (isCameraRunning) {
            stopPreview()
            main_preview_button.text = getString(R.string.button_preview_start_text)
        }
        else {
            if (startPreview()) {
                main_preview_button.text = getString(R.string.button_preview_stop_text)
            }
        }
    }

    private fun startPreview(): Boolean {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
            return true
        }

        Log.e(TAG, "camera permission is not granted.")
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)

        return false
    }

    private fun stopPreview() {
        closeCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty()) {
            return;
        }
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                changeCameraState()
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private fun openCamera() {
        camera = Camera(this)
        camera!!.surface = Surface(main_texture_view.surfaceTexture)
        camera!!.open()
    }

    private fun closeCamera() {
        if (camera == null) {
            return
        }
        camera!!.close()
        camera = null
    }

    private companion object {
        private const val TAG = "MainActivity"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
    }
}
