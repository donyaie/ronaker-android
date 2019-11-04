package com.ronaker.app.utils

import android.app.Activity
import android.os.Build
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by donyaie on 15/07/2016.
 */
class AnimationHelper {
    internal val TAG = AnimationHelper::class.java.name


    companion object {

        fun setFadeTransition(activity: Activity) {
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }


        fun setSlideTransition(activity: AppCompatActivity) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                val slide = Slide()
                slide.slideEdge = Gravity.RIGHT
                slide.excludeTarget(android.R.id.statusBarBackground, true);
                slide.excludeTarget(android.R.id.navigationBarBackground, true);
                activity.window.enterTransition = slide
                activity.window.enterTransition = slide
            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        fun setFadeTransition(activity: AppCompatActivity) {


//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
//                val slide = Fade()
//                slide.excludeTarget(android.R.id.statusBarBackground, true);
//                slide.excludeTarget(android.R.id.navigationBarBackground, true);
//                activity.window.enterTransition = slide
//                activity.window.enterTransition = slide
//            } else
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        fun setAnimateTransition(activity: AppCompatActivity) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
                val slide = Fade()
                slide.excludeTarget(android.R.id.statusBarBackground, true);
                slide.excludeTarget(android.R.id.navigationBarBackground, true);
                activity.window.enterTransition = slide
                activity.window.enterTransition = slide
            } else
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }
}
