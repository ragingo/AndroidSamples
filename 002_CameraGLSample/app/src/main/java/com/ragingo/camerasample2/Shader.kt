package com.ragingo.camerasample2

import android.opengl.GLES31
import android.util.Log


abstract class ShaderBase(sourceCode: String = "") {

    companion object {
        const val INVALID_HANDLE = 0
    }

    var handle = 0
        private set(value) { field = value }

    var sourceCode = sourceCode

    protected abstract val shaderType: Int

    fun compile(): Boolean {
        handle = GLES31.glCreateShader(shaderType)

        val trimmedSourceCode = sourceCode.trim(' ', '\t', '\r', '\n').replace('\t', ' ')
        GLES31.glShaderSource(handle, trimmedSourceCode)
        GLES31.glCompileShader(handle)

        var result = intArrayOf(0)
        GLES31.glGetShaderiv(handle, GLES31.GL_COMPILE_STATUS, result, 0)

        if (result[0] == 0) {
            Log.e("ShaderBase.compile", GLES31.glGetShaderInfoLog(handle))
            GLES31.glDeleteShader(handle)
            handle = INVALID_HANDLE
            return false
        }

        return true

    }
}

class VertexShader(sourceCode: String = "") : ShaderBase(sourceCode) {
    override val shaderType = GLES31.GL_VERTEX_SHADER
}

class FragmentShader(sourceCode: String = "") : ShaderBase(sourceCode) {
    override val shaderType = GLES31.GL_FRAGMENT_SHADER
}