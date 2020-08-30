package com.ronaker.app.utils

import android.app.Application
import android.util.Log
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.onesignal.OneSignalHelper
import com.ronaker.app.data.UserRepository
import com.ronaker.app.data.local.PreferencesProvider


class AppNotificationOpenedHandler(private val app: Application) :
    OneSignal.NotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenResult?) {


        result?.let {

            val actionType = result.action.type
            val data = result.notification.payload.additionalData


            Log.i(
                "OSNotificationPayload",
                "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject()
                    .toString()
            )

            val isLogin=! (PreferencesProvider(app).getString("tokenKey",null)?.isNullOrEmpty()?:true)




            OneSignalHelper.handleNotificationClick(app,data,actionType, isLogin,result)




        }


        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
        /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
    }
}