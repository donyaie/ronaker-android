package com.ronaker.app.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.R
import android.graphics.Color
import androidx.appcompat.content.res.AppCompatResources




class ShapeDrawableHelper {

    // ==== final variables ==== //
    val TAG: String = ShapeDrawableHelper::class.java.name

    companion object {

        fun changeSvgDrawableColor(context:Context, colorRes:Int, view:ImageView){



            DrawableCompat.setTint(view.getDrawable(),ContextCompat.getColor(context,colorRes));
        }

    }

}
