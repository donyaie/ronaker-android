package com.ronaker.app.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.transition.*
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.DecelerateInterpolator
import com.ronaker.app.R


/**
 * Created by donyaie on 15/07/2016.
 */

class AnimationHelper {


    companion object {

        fun setFadeTransition(activity: Activity) {
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        fun setStartSlideTransition(activity: AppCompatActivity){
            activity. overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        fun setEndSlideTransition(activity: AppCompatActivity){
            activity. overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }

        fun clearTransition(activity: AppCompatActivity){
            activity. overridePendingTransition(0,0)
        }

//        fun settSlideTransition(activity: AppCompatActivity) {
//
//
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                activity.window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//////                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
////                val slide = Slide()
////                val reenter = Slide()
////                slide.slideEdge = Gravity.RIGHT
////                reenter.slideEdge = Gravity.LEFT
////
////                slide.excludeTarget(android.R.id.statusBarBackground, true)
////                slide.excludeTarget(android.R.id.navigationBarBackground, true)
////
////
////                reenter.excludeTarget(android.R.id.statusBarBackground, true)
////                reenter.excludeTarget(android.R.id.navigationBarBackground, true)
////
////
////                activity.window.enterTransition = slide
////                activity.window.reenterTransition = reenter
//////                activity.window.exitTransition = exit
////
////            } else
////                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//        }
//
//        private fun enterTransition(): Transition {
//            val bounds = ChangeBounds()
//            bounds.duration = 500
//
//            return bounds
//        }
//
//        private fun returnTransition(): Transition {
//            val bounds = ChangeBounds()
//            bounds.interpolator = DecelerateInterpolator()
//            bounds.duration = 500
//
//            return bounds
//        }





//        fun setAnimateTransition(activity: AppCompatActivity) {
//
//
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//////                activity.window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
////                val slide = Fade()
//////                slide.setDuration(500)
////                slide.excludeTarget(android.R.id.statusBarBackground, true)
////                slide.excludeTarget(android.R.id.navigationBarBackground, true)
////                activity.window.enterTransition = slide
////                activity.window.exitTransition = null
//////                activity.window.sharedElementEnterTransition=enterTransition()
//////                activity.window.sharedElementReturnTransition=returnTransition()
////            } else
////                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//        }


    }
}
