package com.ragingo.sample.jetpack.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ragingo.sample.jetpack.R
import com.ragingo.sample.jetpack.data.UserInfo
import com.ragingo.sample.jetpack.databinding.ActivityMainBinding
import com.ragingo.sample.jetpack.viewmodels.main.MainViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindings: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val user1 = UserInfo()
        user1.id = "u001"
        user1.name = "user 1"
        user1.age = 20

        val vm = ViewModelProviders.of(this).get(MainViewModel::class.java)
        vm.userInfo.value!!.loadFromModel(user1)
        vm.userInfo.observe(this, Observer {
            Log.d(TAG, "${it.id}, ${it.name}, ${it.age}")
        })

        bindings.vm = vm
        bindings.lifecycleOwner = this
    }

}
