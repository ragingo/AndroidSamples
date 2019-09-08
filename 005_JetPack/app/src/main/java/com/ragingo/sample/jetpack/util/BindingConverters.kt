package com.ragingo.sample.jetpack.util

import android.view.View
import androidx.databinding.BindingConversion

object BindingConverters {

    @BindingConversion
    @JvmStatic
    fun booleanToVisibility(visible: Boolean) : Int {
        return if (visible) View.VISIBLE else View.GONE
    }

}
