package com.ragingo.sample.jetpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ragingo.sample.jetpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindings: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindings.user = UserInfo("user1");
    }

}
