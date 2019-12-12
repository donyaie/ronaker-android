package com.ronaker.app.utils

import android.app.Activity
import android.os.Build
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.DecelerateInterpolator




/**
 * Created by donyaie on 15/07/2016.
 */
class AnimationHelper {
    private val TAG = AnimationHelper::class.java.name


    companion object {

        fun setFadeTransition(activity: Activity) {
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }


        fun setSlideTransition(activity: AppCompatActivity) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                val slide = Slide()
                slide.slideEdge = Gravity.END
                slide.excludeTarget(android.R.id.statusBarBackground, true)
                slide.excludeTarget(android.R.id.navigationBarBackground, true)
                activity.window.enterTransition = slide
                activity.window.enterTransition = slide

            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        private fun enterTransition(): Transition {
            val bounds = ChangeBounds()
            bounds.duration = 500

            return bounds
        }

        private fun returnTransition(): Transition {
            val bounds = ChangeBounds()
            bounds.interpolator = DecelerateInterpolator()
            bounds.duration = 500

            return bounds
        }

        fun setFadeTransition(activity: AppCompatActivity) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                val slide = Fade()
                slide.excludeTarget(android.R.id.statusBarBackground, true)
                slide.excludeTarget(android.R.id.navigationBarBackground, true)
                activity.window.enterTransition = slide
                activity.window.enterTransition = slide
            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        fun setAnimateTransition(activity: AppCompatActivity) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                val slide = Fade()
//                slide.setDuration(500)
                slide.excludeTarget(android.R.id.statusBarBackground, true)
                slide.excludeTarget(android.R.id.navigationBarBackground, true)
                activity.window.enterTransition = slide
                activity.window.enterTransition = slide
//                activity.window.sharedElementEnterTransition=enterTransition()
//                activity.window.sharedElementReturnTransition=returnTransition()
            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }
}
