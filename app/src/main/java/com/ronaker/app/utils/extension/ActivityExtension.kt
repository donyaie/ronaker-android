package com.ronaker.app.utils.extension

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.ronaker.app.utils.AnimationHelper

fun Activity.finishSafe(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        AnimationHelper.setAnimateTransition(this as AppCompatActivity)
        this.finishAfterTransition()
    }
    else {

        AnimationHelper.setAnimateTransition(this as AppCompatActivity)
        this.finish()
    }
}


fun Activity.startActivityMakeScene(intent: Intent?){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }else{
        this.startActivity(intent)
    }
}

fun Activity.startActivityMakeSceneForResult(intent: Intent?,requestCode:Int){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.startActivityForResult(intent,requestCode, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }else{
        this.startActivityForResult(intent,requestCode)
    }
}





