package com.ronaker.app.utils

import android.app.Activity
import android.os.Build
import android.transition.*
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


        fun setReenterAnimation(activity: AppCompatActivity){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

                val reenter = Slide()
                reenter.slideEdge = Gravity.START
                reenter.excludeTarget(android.R.id.statusBarBackground, true)
                reenter.excludeTarget(android.R.id.navigationBarBackground, true)
                activity.window.reenterTransition = reenter

            }
        }


        fun setSlideTransition(activity: AppCompatActivity) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                val slide = Slide()
                val reenter = Slide()
                slide.slideEdge = Gravity.END
                reenter.slideEdge = Gravity.START

                slide.excludeTarget(android.R.id.statusBarBackground, true)
                slide.excludeTarget(android.R.id.navigationBarBackground, true)


                reenter.excludeTarget(android.R.id.statusBarBackground, true)
                reenter.excludeTarget(android.R.id.navigationBarBackground, true)


                activity.window.enterTransition = slide
                activity.window.reenterTransition = reenter
//                activity.window.exitTransition = exit

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
                activity.window.exitTransition = null
            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        fun setAnimateTransition(activity: AppCompatActivity) {


//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
//                val slide = Fade()
////                slide.setDuration(500)
//                slide.excludeTarget(android.R.id.statusBarBackground, true)
//                slide.excludeTarget(android.R.id.navigationBarBackground, true)
//                activity.window.enterTransition = slide
//                activity.window.exitTransition = null
////                activity.window.sharedElementEnterTransition=enterTransition()
////                activity.window.sharedElementReturnTransition=returnTransition()
//            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }
}
