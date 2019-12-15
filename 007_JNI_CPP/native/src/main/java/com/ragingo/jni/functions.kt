package com.ragingo.jni

object NativeFunctions {
    init {
        System.loadLibrary("native")
    }
    external fun ret100(): Int
}

