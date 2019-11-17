package com.ronaker.app.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.onesignal.OneSignal

object AnalyticsManager {


    fun setName(analytics: FirebaseAnalytics,id:String,name:String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

    }


    fun setUserIdTag(userId:String){
        OneSignal.sendTag("user_id",userId)
    }
}