package com.ragingo.camerasample2

import android.opengl.GLES31
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class GraphicsUtils {

    companion object {
        fun makeTexture(): Int {
            var texIds = intArrayOf(0)
            GLES31.glGenTextures(1, texIds, 0)
            return texIds[0]
        }

        fun makeUVBuffer(displayOrientation: Int): FloatBuffer {
            val buf = when (displayOrientation) {
                0  -> floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f);
                90  -> floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
                180  -> floatArrayOf(1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);
                270  -> floatArrayOf(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                else -> floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f);
            }
            return makeFloatBuffer(buf)
        }

        fun makeVertexBuffer(x: Int, y: Int, width: Int, height: Int, screenWidth: Int, screenHeight: Int): FloatBuffer {
            var left = (x.toFloat() / screenWidth.toFloat()) * 2.0f - 1.0f;
            var top = (y.toFloat() / screenHeight.toFloat()) * 2.0f - 1.0f;
            var right = ((x + width).toFloat() / screenWidth.toFloat()) * 2.0f - 1.0f;
            var bottom = ((y + height).toFloat() / screenHeight.toFloat()) * 2.0f - 1.0f;
            top = -top;
            bottom = -bottom;

            // 頂点バッファの生成
            val vertices = floatArrayOf(
                left,  top,     0.0f, // 頂点0
                left,  bottom,  0.0f, // 頂点1
                right, top,     0.0f, // 頂点2
                right, bottom,  0.0f  // 頂点3
            );

            return makeFloatBuffer(vertices)
        }

        fun makeFloatBuffer(data: FloatArray): FloatBuffer {
            val buf =
                ByteBuffer.allocateDirect(data.count() * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()

            buf.put(data).position(0)
            return buf
        }
    }
}