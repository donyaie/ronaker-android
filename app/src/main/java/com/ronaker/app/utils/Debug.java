package com.ronaker.app.utils;

import android.util.Log;

public class Debug {

    public static void Log(String TAG, String Message) {
        Log.d(TAG, Message);
    }

    public static void Log(String TAG, Exception ex) {
        Log(TAG, "Exception ", ex);

    }

    public static void Log(String TAG, String Message, Exception ex) {
        if (ex != null)
            Message = Message + " --> " + ex.getMessage();

        Log.e(TAG, Message);
        if (ex != null)
            ex.printStackTrace();
    }

}
