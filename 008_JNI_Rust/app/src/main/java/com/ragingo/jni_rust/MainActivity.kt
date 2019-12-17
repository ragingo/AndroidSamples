package com.ragingo.jni_rust

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ragingo.jni.NativeFunctions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = NativeFunctions.ret121().toString()
    }
}
