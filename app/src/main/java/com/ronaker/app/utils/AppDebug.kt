package com.ronaker.app.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ronaker.app.BuildConfig

object AppDebug {

    fun log(TAG: String, Message: String?) {


        Message?.let {
            if (BuildConfig.DEBUG)
                Log.d(TAG, it)
            else
                FirebaseCrashlytics.getInstance().log( it)



        }
    }

    private fun logError(TAG: String, Message: String?) {


        Message?.let {
            if (BuildConfig.DEBUG)
                Log.e(TAG, it)
            else
                FirebaseCrashlytics.getInstance().log("$TAG $it")
        }
    }

    fun log(TAG: String, ex: Exception) {
        log(TAG, "Exception ", ex)

    }


    fun log(TAG: String, ex: Throwable) {

        log(TAG, "Throwable ", ex)

    }

    fun log(TAG: String, Message: String?, ex: Throwable) {


        logError(TAG, (Message ?: "") + " --> " + ex.message)

        if (BuildConfig.DEBUG)
            ex.printStackTrace()
        else
            FirebaseCrashlytics.getInstance().recordException(ex)


    }

}
