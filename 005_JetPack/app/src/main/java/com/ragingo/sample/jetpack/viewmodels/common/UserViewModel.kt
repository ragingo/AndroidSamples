package com.ragingo.sample.jetpack.viewmodels.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ragingo.sample.jetpack.data.UserInfo

class UserViewModel() : ViewModel() {

    val id = MutableLiveData("")
    val name = MutableLiveData("")
    val age = MutableLiveData(-1)

    fun loadFromModel(model: UserInfo) {
        id.value = model.id
        name.value = model.name
        age.value = model.age
    }

}
