package com.ronaker.app.utils

import android.app.Activity


/**
 * Created by donyaie on 15/07/2016.
 */
class AnimationHelper {
    internal val TAG = AnimationHelper::class.java.name



    companion object {

        fun animateActivityFade(activity: Activity) {
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }
}
