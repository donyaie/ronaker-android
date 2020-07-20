package com.ronaker.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.provider.Settings


object IntentManeger {

    private val TAG = IntentManeger::class.java.name

    fun openSettings(activity: Activity, requestCode: Int) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            activity.startActivityForResult(intent, requestCode)
        } catch (ex: Exception) {

            AppDebug.log(TAG, ex)
        }
    }


    fun openUrl(context: Context, url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            browserIntent.flags = FLAG_ACTIVITY_NEW_TASK

            context.startActivity(browserIntent)
        } catch (ex: Exception) {
            AppDebug.log(TAG, ex)
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

            AppDebug.log(TAG, ex)
        }
    }


    fun sendMail(context: Context, email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.data = Uri.parse("mailto:$email")
            val chooserIntent = Intent.createChooser(intent, "Send Email")
            chooserIntent.flags = FLAG_ACTIVITY_NEW_TASK

            context.startActivity(chooserIntent)
        } catch (ex: java.lang.Exception) {

            AppDebug.log(TAG, ex)
        }
    }


    fun openMailBox(context: Context) {
        try {
//
//            val intent = Intent(Intent.ACTION_SENDTO)
//            intent.type = "text/plain"
//            intent.data = Uri.parse("mailto:")

            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)


            val chooserIntent = Intent.createChooser(intent, "Open Mail Box")
            chooserIntent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooserIntent)

        } catch (ex: Exception) {

            AppDebug.log(TAG, ex)
        }
    }


    fun makeCall(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse("tel:$phone")
            context.startActivity(intent)
        } catch (ex: java.lang.Exception) {

            AppDebug.log(TAG, ex)
        }
    }


}


