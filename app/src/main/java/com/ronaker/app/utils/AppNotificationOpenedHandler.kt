package com.ronaker.app.utils

import android.app.Application
import android.util.Log
import com.onesignal.OSNotificationAction.ActionType
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity


class AppNotificationOpenedHandler(private val app: Application) : OneSignal.NotificationOpenedHandler{
    override fun notificationOpened(result: OSNotificationOpenResult?) {


        result?.let {

            val actionType = result.action.type
            val data = result.notification.payload.additionalData


            Log.i(
                "OSNotificationPayload",
                "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject().toString()
            )


            if (data != null && data.has("order_suid")) {
                val orderId:String?=data.optString("order_suid")

                if (orderId != null) {

                    app.startActivity(OrderPreviewActivity.newInstance(app,orderId))

                    Log.i(
                        "OneSignalExample",
                        "customkey set with value: $orderId"
                    )
                }
            }

            if (actionType == ActionType.ActionTaken) Log.i(
                "OneSignalExample",
                "Button pressed with id: " + result.action.actionID
            )

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