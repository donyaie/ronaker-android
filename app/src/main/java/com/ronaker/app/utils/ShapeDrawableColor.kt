package com.ronaker.app.utils

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.android.synthetic.main.fragment_product_add_price.view.*


class ShapeDrawableHelper {


    companion object {

        // ==== final variables ==== //
        val TAG: String = ShapeDrawableHelper::class.java.name
        fun changeSvgDrawableColor(context:Context, colorRes:Int, view:ImageView){
            try {
                if (Build.VERSION.SDK_INT < 21) {
                    view.setColorFilter(ContextCompat.getColor(context, colorRes), PorterDuff.Mode.SRC_IN)
                }else
                DrawableCompat.setTint(view.drawable, ContextCompat.getColor(context, colorRes))

            }catch (e:Exception)
            {
                e.printStackTrace()
            }
        }

    }

}
