package com.ronaker.app.utils

import android.app.Activity
import android.content.Context
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


    fun openUrl(context: Context, url:String){
        try{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }catch(ex:Exception) {
            ex.printStackTrace()
        }
    }

    fun sendMail(context: Context, email:String){
        val intent = Intent(Intent.ACTION_SENDTO);
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:$email}")
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    }




    fun makeCall(context: Activity, phone:String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        context. startActivity(intent)
    }


}


