package com.ronaker.app.utils

import android.app.Activity
import android.os.Build
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


        fun setSlideTransition(activity :AppCompatActivity){


//            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val slide = Slide()
                slide.slideEdge = Gravity.BOTTOM


                activity.window.enterTransition = slide

            }
        }

    }
}
