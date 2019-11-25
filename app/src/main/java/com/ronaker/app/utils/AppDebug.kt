package com.ronaker.app.utils

import android.util.Log

object AppDebug {

    fun log(TAG: String, Message: String?) {
        if (Message != null)
            Log.d(TAG, Message)
    }

    fun log(TAG: String, ex: Exception?) {
        if (ex != null)
            log(TAG, "Exception ", ex)

    }

    fun log(TAG: String, Message: String?, ex: Exception?) {

        var message1 = Message?:""
        if (ex != null)
            message1 = message1 + " --> " + ex.message

        Log.e(TAG, message1)
        ex?.printStackTrace()
    }

}
