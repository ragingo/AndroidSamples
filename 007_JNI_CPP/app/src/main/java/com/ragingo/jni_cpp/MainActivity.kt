package com.ragingo.jni_cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ragingo.jni.NativeFunctions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val str =
            NativeFunctions.ret100().toString() + "\n" +
            NativeFunctions.dpi().toString() + "\n" +
            NativeFunctions.fpi().toString() + "\n" +
            NativeFunctions.lpi().toString() + "\n" +
            NativeFunctions.ipi().toString() + "\n"

        textView.text = str
    }
}
