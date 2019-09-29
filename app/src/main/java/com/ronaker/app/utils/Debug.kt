package com.ronaker.app.utils;

import android.util.Log

object Debug {

    fun Log(TAG: String, Message: String?) {
        if (Message != null)
            Log.d(TAG, Message)

    }

    fun Log(TAG: String, ex: Exception?) {
        if (ex != null)
            Log(TAG, "Exception ", ex)

    }

    fun Log(TAG: String, Message: String?, ex: Exception?) {

        var message1 = Message?:""
        if (ex != null)
            message1 = message1 + " --> " + ex.message

        Log.e(TAG, message1)
        ex?.printStackTrace()
    }

}
