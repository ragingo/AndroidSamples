package com.ragingo.jni_cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ragingo.jni.NativeFunctions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sb = StringBuilder()
        sb.appendln(NativeFunctions.ret100())
        sb.appendln(NativeFunctions.dpi())
        sb.appendln(NativeFunctions.fpi())
        sb.appendln(NativeFunctions.lpi())
        sb.appendln(NativeFunctions.ipi())
        sb.appendln(NativeFunctions.pow(2, 3))

        textView.text = sb.toString()
    }
}
