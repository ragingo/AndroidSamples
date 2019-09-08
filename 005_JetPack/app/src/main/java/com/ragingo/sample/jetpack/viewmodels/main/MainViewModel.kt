package com.ragingo.sample.jetpack.viewmodels.main

import androidx.lifecycle.ViewModel
import com.ragingo.sample.jetpack.data.UserInfo

class MainViewModel : ViewModel() {

    var userInfo: UserInfo = UserInfo()
        private set
}
