package com.ragingo.sample.jetpack.viewmodels.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ragingo.sample.jetpack.viewmodels.common.UserViewModel

class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.simpleName
    }

    private val _userInfo = MutableLiveData(UserViewModel())
    val userInfo: LiveData<UserViewModel> = _userInfo

    fun addAge() {
        Log.d(TAG, "addAge")
        _userInfo.value?.let {
            x -> x.age.value?.let {
                y -> x.age.value = y + 1;
            }
        }
    }
}
