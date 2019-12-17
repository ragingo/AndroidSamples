package com.ragingo.jni

object NativeFunctions {
    init {
        System.loadLibrary("rust")
    }
    external fun ret121(): Int
}