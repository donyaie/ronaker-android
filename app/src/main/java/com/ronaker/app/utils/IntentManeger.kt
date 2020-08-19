package com.ronaker.app.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File


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


    fun openFacebook(context: Context, Id: String, Name: String) {
        val intent = try {
            context.packageManager
                .getPackageInfo("com.facebook.orca", 0) //Checks if FB is even installed.
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://m.me/$Name")
            ) .apply { setPackage("com.facebook.orca") }

            //Trys to make intent with FB's URI
        } catch (e: java.lang.Exception) {

            try {
                context.packageManager
                    .getPackageInfo("com.facebook.katana", 0) //Checks if FB is even installed.
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("fb://page/$Id")
                ) .apply { setPackage("com.facebook.katana") }

                //Trys to make intent with FB's URI
            } catch (e: java.lang.Exception) {

                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://m.me/$Name")
                ) //catches and opens a url to the desired page
            }
        }

        try {
            context.startActivity(intent)

        } catch (ex: Exception) {

            AppDebug.log(TAG,ex)

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


    fun pickImage(context: Activity, req: Int) {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        context.startActivityForResult(pickPhoto, req)
    }


    fun takePicture(context: Activity, fileName: String, req: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileUtils.getCacheCameraPath(context, fileName)
        )

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            takePictureIntent.clipData =
                ClipData.newRawUri(
                    "",
                    FileUtils.getCacheCameraPath(
                        context,
                        fileName
                    )
                )
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            context.startActivityForResult(takePictureIntent, req)
        }


    }


    fun openPDF(context: Activity, file: File) {

        val target = Intent(Intent.ACTION_VIEW)
//        target.setDataAndType(Uri.fromFile(file), "application/pdf")


        val apkURI = FileProvider.getUriForFile(
            context, "${context.applicationContext.packageName}.provider", file
        )

        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        target.setDataAndType(apkURI, "application/pdf")


        val intent = Intent.createChooser(target, "Open File")
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
        }
    }


}


