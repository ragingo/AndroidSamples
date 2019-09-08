package com.ragingo.sample.jetpack.util

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {

    private val TAG = BindingAdapters::class.simpleName

    @BindingAdapter("app:foo")
    @JvmStatic
    fun foo(view: View, foo: Any) {
        Log.d(TAG, "bar: $foo")
    }

}
