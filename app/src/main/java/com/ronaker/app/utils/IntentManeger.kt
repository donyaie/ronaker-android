package com.ronaker.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.provider.Settings


object IntentManeger {


    fun openSettings(activity: Activity, requestCode: Int) {

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        activity.startActivityForResult(intent, requestCode)
    }


    fun openUrl(context: Context, url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            browserIntent.flags = FLAG_ACTIVITY_NEW_TASK

            context.startActivity(browserIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


     fun shareTextUrl(context: Context, title: String, url: String) {
        try {

            val share = Intent(Intent.ACTION_SEND).apply {

                type = "text/plain"
                //putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, url)

            }

            val shareIntent = Intent.createChooser(share, title)
            shareIntent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(shareIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun sendMail(context: Context, email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:$email}")
        val chooserIntent =  Intent.createChooser(intent, "Send Email")
        chooserIntent.flags = FLAG_ACTIVITY_NEW_TASK

        context.startActivity(chooserIntent)
    }


    fun makeCall(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("tel:$phone")
        context.startActivity(intent)
    }


}


