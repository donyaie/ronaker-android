package com.ronaker.app.utils.kayboardAnimator

import android.annotation.TargetApi
import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
class SimpleKeyboardAnimator(window: Window) : BaseKeyboardAnimator(window) {

    private val sceneRoot: ViewGroup? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)?.parent as? ViewGroup
    }

    override val insetsListener: View.OnApplyWindowInsetsListener
        get() = View.OnApplyWindowInsetsListener { view, insets ->


            val transition= ChangeBounds()
            sceneRoot?.let { TransitionManager.beginDelayedTransition(it, transition) }
            return@OnApplyWindowInsetsListener view.onApplyWindowInsets(insets)
        }
}