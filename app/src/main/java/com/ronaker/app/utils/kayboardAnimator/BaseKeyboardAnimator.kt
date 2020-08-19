package com.ronaker.app.utils.kayboardAnimator

import android.view.View
import android.view.Window
import android.view.WindowManager

abstract class BaseKeyboardAnimator(private val window: Window) {

    protected abstract val insetsListener: View.OnApplyWindowInsetsListener

    init {
        //TODO : https://proandroiddev.com/exploring-windowinsets-on-android-11-a80cf8fe19be
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    fun start() = window.decorView.setOnApplyWindowInsetsListener(insetsListener)

    fun stop() = window.decorView.setOnApplyWindowInsetsListener(null)
}