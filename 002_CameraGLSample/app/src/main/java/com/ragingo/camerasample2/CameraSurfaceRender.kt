package com.ragingo.camerasample2

import android.Manifest
import android.app.Activity
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES31
import android.view.Surface
import androidx.annotation.RequiresPermission
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraSurfaceRender(private val activity: Activity) : SurfaceRender() {

    private var surfaceTexture: SurfaceTexture? = null
    private var textureId = 0
    private var vertexBuffer = FloatBuffer.allocate(0)
    private var uvBuffer = FloatBuffer.allocate(0)
    private var textureHandle = 0
    private var positionHandle = 0
    private var uvHandle = 0
    private var camera: Camera? = null

    var effectType = EffectType.None

    val isCameraRunning: Boolean
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

    init {
        shaders.add(VertexShader(CameraShaderSources.vertexShader))
        shaders.add(FragmentShader(CameraShaderSources.fragmentShader))
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    fun startPreview() {
        openCamera()
    }

    fun stopPreview() {
        closeCamera()
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private fun openCamera() {
        textureId = GraphicsUtils.makeTexture()
        surfaceTexture = SurfaceTexture(textureId)
        surfaceTexture!!.setDefaultBufferSize(screenWidth, screenHeight)

        camera = Camera(activity)
        camera!!.surfaceTexture = surfaceTexture
        camera!!.open()
    }

    private fun closeCamera() {
        if (camera == null) {
            return
        }
        surfaceTexture?.release()
        surfaceTexture = null
        camera!!.close()
        camera = null
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        uvHandle = GLES31.glGetAttribLocation(programHandle, "in_texcoord")
        positionHandle = GLES31.glGetAttribLocation(programHandle, "in_position")
        textureHandle = GLES31.glGetUniformLocation(programHandle, "u_Tex")
        GLES31.glEnableVertexAttribArray(uvHandle)
        GLES31.glEnableVertexAttribArray(positionHandle)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)

        val displayRotation =
            when (activity.windowManager.defaultDisplay.rotation) {
                Surface.ROTATION_0 -> 270
                Surface.ROTATION_90 -> 0
                Surface.ROTATION_180 -> 90
                Surface.ROTATION_270 -> 180
                else -> 0
            }
        uvBuffer = GraphicsUtils.makeUVBuffer(displayRotation)
        vertexBuffer = GraphicsUtils.makeVertexBuffer(0, 0, screenWidth, screenHeight, screenWidth, screenHeight)
    }

    override fun onDraw() {
        if (!isCameraRunning) {
            return
        }

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
        GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES31.glUniform1i(textureHandle, 0)
        GLES31.glVertexAttribPointer(uvHandle, 2, GLES31.GL_FLOAT, false, 0, uvBuffer)
        GLES31.glVertexAttribPointer(positionHandle, 3, GLES31.GL_FLOAT, false, 0, vertexBuffer)
        GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR.toFloat())
        GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR.toFloat())
        GLES31.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE)
        GLES31.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE)
        GLES31.glUniform1i(GLES31.glGetUniformLocation(programHandle, "u_EffectType"), effectType.value)
        GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4);

        GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)

        try {
            // 頻繁に落ちる・・・とりあえず無視
            // TODO: 直す
            surfaceTexture?.updateTexImage()
        }
        catch (e: Exception){
            e.printStackTrace()
        }

    }

    enum class EffectType(val value: Int) {
        None(0),
        Grayscale(1),
        Binarization(2)
    }
    
    private companion object {
        private const val TAG = "CameraSurfaceRender"
    }
}

class CameraShaderSources {

    companion object {

        // 頂点シェーダーコード
        const val vertexShader = """
            #version 300 es
            #extension GL_OES_EGL_image_external : require
            #extension GL_OES_EGL_image_external_essl3 : enable
            in vec4 in_position;
            in vec2 in_texcoord;
            out vec2 out_texcoord;
            void main() {
                gl_Position = in_position;
                out_texcoord = in_texcoord;
            }
        """

        // フラグメントシェーダーコード
        const val fragmentShader = """
            #version 300 es
            #extension GL_OES_EGL_image_external : require
            #extension GL_OES_EGL_image_external_essl3 : enable

            precision mediump float;

            in vec2 out_texcoord;
            layout (location = 0) out vec4 fragColor;
            uniform samplerExternalOES u_Tex;
            uniform int u_EffectType;

            // Effect Types
            const int EFFECT_TYPE_NONE = 0;
            const int EFFECT_TYPE_GRAYSCALE = 1;
            const int EFFECT_TYPE_BINARIZATION = 2;

            vec4 grayscale(vec4 color) {
                float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
                return vec4(gray, gray, gray, color.a);
            }

            vec4 binarization(vec4 color, float threshold) {
                if (color.r > threshold ||
                    color.g > threshold ||
                    color.b > threshold) {
                    color = vec4(0.0, 0.0, 0.0, 1.0);
                }
                else {
                    color = vec4(1.0, 1.0, 1.0, 1.0);
                }
                return color;
            }

            void main() {
                vec4 outputColor;
                if (u_EffectType == EFFECT_TYPE_NONE) {
                    outputColor = texture(u_Tex, out_texcoord);
                }
                else if (u_EffectType == EFFECT_TYPE_GRAYSCALE) {
                    vec4 color = texture(u_Tex, out_texcoord);
                    outputColor = grayscale(color);
                }
                else if (u_EffectType == EFFECT_TYPE_BINARIZATION) {
                    vec4 color = texture(u_Tex, out_texcoord);
                    outputColor = binarization(color, 0.5);
                }
                fragColor = outputColor;
            }
        """

    }
}