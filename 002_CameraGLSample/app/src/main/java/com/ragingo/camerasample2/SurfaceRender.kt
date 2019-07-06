package com.ragingo.camerasample2

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


open class SurfaceRender : GLSurfaceView.Renderer {

    val shaders = mutableListOf<ShaderBase>()

    var programHandle = 0
        private set(value) { field = value }

    var screenWidth = 0
        private set(value) { field = value }

    var screenHeight = 0
        private set(value) { field = value }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        programHandle = GLES31.glCreateProgram()

        shaders.forEach {
            if (it.compile()) {
                GLES31.glAttachShader(programHandle, it.handle)
            }
        }

        GLES31.glLinkProgram(programHandle)
        GLES31.glUseProgram(programHandle)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height
    }

    open fun close() {
        shaders.forEach {
            GLES31.glDeleteShader(it.handle)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        onBeginDraw()
        onDraw()
        onEndDraw()
    }

    open fun onDraw() {
    }

    open fun onBeginDraw() {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
    }

    open fun onEndDraw() {
        GLES31.glDisable(GLES31.GL_BLEND);
    }

}