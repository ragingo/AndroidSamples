package com.ragingo.camerasample

import android.Manifest
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.support.annotation.RequiresPermission
import android.util.Log
import android.view.Surface

class Camera(ctx: Context) {

    private var cameraDevice: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null
    private val cameraManager: CameraManager = ctx.getSystemService(Context.CAMERA_SERVICE) as CameraManager;
    private var surface: Surface? = null

    var surfaceTexture: SurfaceTexture? = null
    var state: State = State.Close
        private set(value) {
            field = value
        }

    @RequiresPermission(Manifest.permission.CAMERA)
    fun open() {
        val cameraId =
            cameraManager.cameraIdList.firstOrNull {
                cameraManager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
            }

        if (cameraId.isNullOrEmpty()) {
            Log.e(TAG, "camera not found.")
            return
        }

        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val configMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val size = configMap.getOutputSizes(SurfaceTexture::class.java).maxBy { it.width * it.height }

        if (size == null) {
            Log.e(TAG, "output size not found.")
            return
        }

        surfaceTexture!!.setDefaultBufferSize(size.width, size.height)

        cameraManager.openCamera(cameraId, object: CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                state = State.Open
                surface = Surface(surfaceTexture)

                createCameraCaptureSession()
            }

            override fun onDisconnected(camera: CameraDevice) {
                close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                close()
            }
        }, null)
    }

    fun close() {
        state = State.Close
        surface?.release()
        surface = null
        cameraSession?.close()
        cameraSession = null
        cameraDevice?.close()
        cameraDevice = null
    }

    private fun createCameraCaptureSession() {
        assert(cameraDevice != null)
        assert(surfaceTexture != null)
        assert(surface != null)
        cameraDevice!!.createCaptureSession(listOf(surface), stateCallback, null)
    }

    private val stateCallback = object: CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraSession = session
            state = State.Capture

            val builder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            builder.addTarget(surface!!)
            session.setRepeatingRequest(builder.build(), null, null)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
        }
    }

    enum class State {
        Open,
        Capture,
        Close
    }

    private companion object {
        private const val TAG = "Camera"
    }

}