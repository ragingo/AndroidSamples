package com.ragingo.camerasample2

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val cameraRender = CameraSurfaceRender(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_preview_button.setOnClickListener {
            changeCameraState()
        }

        main_radiogroup_effect.visibility = View.INVISIBLE
        radio_effect_none.isChecked = true
        radio_effect_none.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cameraRender.effectType = CameraSurfaceRender.EffectType.None
            }
        }
        radio_effect_grayscale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cameraRender.effectType = CameraSurfaceRender.EffectType.Grayscale
            }
        }
        radio_effect_binarization.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cameraRender.effectType = CameraSurfaceRender.EffectType.Binarization
            }
        }

        main_camera_view.setEGLContextClientVersion(3)
        main_camera_view.setRenderer(cameraRender)
        main_camera_view.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    private fun changeCameraState() {
        if (cameraRender.isCameraRunning) {
            main_preview_button.text = getString(R.string.button_preview_start_text)
            main_radiogroup_effect.visibility = View.INVISIBLE
            cameraRender.stopPreview()
        }
        else {
            if (checkPermission()) {
                main_radiogroup_effect.visibility = View.VISIBLE
                main_preview_button.text = getString(R.string.button_preview_stop_text)
                cameraRender.startPreview()
            }
        }
    }

    private fun checkPermission() : Boolean {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        Log.e(TAG, "camera permission is not granted.")
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        return false
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

    private companion object {
        private const val TAG = "MainActivity"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
    }
}
