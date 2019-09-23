package com.ronaker.app.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings


object IntentManeger{



    fun openSettings(activity:Activity,requestCode:Int) {

        var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        var uri: Uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }



}


