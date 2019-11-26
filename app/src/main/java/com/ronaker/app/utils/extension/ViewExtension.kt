package com.ronaker.app.utils.extension

import android.app.Application
import android.content.ContextWrapper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

fun View.getParentActivity(): AppCompatActivity?{
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.getApplication(): Application{
    val context = this.context.applicationContext

    return context as Application
}

fun Button.setEndDrawableRes(res:Int){
//    this.setCompoundDrawablesRelative(null,null,ContextCompat.getDrawable(context,res),null)
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,res,0)


}







