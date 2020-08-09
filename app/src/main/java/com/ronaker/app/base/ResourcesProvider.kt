package com.ronaker.app.base

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class ResourcesRepository (val context: Context){



    fun getString(resID:Int):String {
        return context.getString(resID)

    }
//    fun getString(resId: Int,vararg args: Any?): String {
//       return String.format( context.getString(resId),args)
//
//    }

    fun getColor( @ColorRes id:Int): Int {
        return ContextCompat.getColor(context, id)
    }




}