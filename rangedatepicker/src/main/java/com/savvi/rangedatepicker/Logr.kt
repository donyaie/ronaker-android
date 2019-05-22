package com.savvi.rangedatepicker

import android.util.Log

/** Log utility class to handle the log tag and DEBUG-only logging.  */
internal object Logr {
    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("TimesSquare", message)
        }
    }

    fun d(message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            d(String.format(message, *args))
        }
    }
}
