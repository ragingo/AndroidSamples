package com.ragingo.sample.jetpack.viewmodels.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ragingo.sample.jetpack.data.UserInfo

class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.simpleName
    }

    private val _userInfo = MutableLiveData(UserInfo())
    val userInfo: LiveData<UserInfo> = _userInfo

    fun addAge() {
        Log.d(TAG, "addAge")
        val info = _userInfo.value!!
        info.age++
        _userInfo.postValue(info)
    }
}
