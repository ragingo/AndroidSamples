package com.ragingo.jni

object NativeFunctions {
    init {
        System.loadLibrary("native")
    }
    external fun ret100(): Int
    external fun pow(x: Int, y: Int): Int
    external fun dpi(): Double
    external fun fpi(): Float
    external fun lpi(): Long
    external fun ipi(): Int
}

