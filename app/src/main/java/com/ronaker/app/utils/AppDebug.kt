package com.ronaker.app.utils

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.ronaker.app.BuildConfig

object AppDebug {

    fun log(TAG: String, Message: String?) {


        Message?.let {
            if (BuildConfig.DEBUG)
                Log.d(TAG, it)
            else
                Crashlytics.log(Log.DEBUG, TAG, it)
        }
    }

    fun log(TAG: String, ex: Exception?) {
        ex?.let {
            log(TAG, "Exception ", ex)
        }
    }

    fun log(TAG: String, Message: String?, ex: Exception?) {

        var message1 = Message ?: ""

        if (ex != null)
            message1 = message1 + " --> " + ex.message

        if (BuildConfig.DEBUG)
            Log.e(TAG, message1)
        else
            Crashlytics.log(Log.DEBUG, TAG, message1)


        ex?.let {

            if (BuildConfig.DEBUG)
                it.printStackTrace()
            else
                Crashlytics.logException(ex)
        }
    }

}
