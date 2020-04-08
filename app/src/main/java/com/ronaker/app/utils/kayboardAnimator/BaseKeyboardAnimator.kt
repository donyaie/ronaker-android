package com.ronaker.app.utils.kayboardAnimator

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
abstract class BaseKeyboardAnimator(private val window: Window) {

    protected abstract val insetsListener: View.OnApplyWindowInsetsListener

    init {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    fun start() = window.decorView.setOnApplyWindowInsetsListener(insetsListener)

    fun stop() = window.decorView.setOnApplyWindowInsetsListener(null)
}