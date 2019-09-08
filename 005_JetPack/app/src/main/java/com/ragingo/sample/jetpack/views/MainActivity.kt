package com.ragingo.sample.jetpack.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ragingo.sample.jetpack.R
import com.ragingo.sample.jetpack.databinding.ActivityMainBinding
import com.ragingo.sample.jetpack.viewmodels.main.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindings: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val vm = ViewModelProviders.of(this).get(MainViewModel::class.java)
        vm.userInfo.value!!.id = "u001"
        vm.userInfo.value!!.name = "user 1"
        vm.userInfo.value!!.age = 20

        bindings.vm = vm
        bindings.lifecycleOwner = this
    }

}
