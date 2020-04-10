package com.ronaker.app.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class ShapeDrawableHelper {


    companion object {

        // ==== final variables ==== //
        val TAG: String = ShapeDrawableHelper::class.java.name
        fun changeSvgDrawableColor(context: Context, iconRes: Int, colorRes: Int, view: ImageView) {
            try {




//
//                view.setImageDrawable(null)
//                view.clearColorFilter()


//                if(view.tag!=null && view.tag==iconRes){
//
//                    AppDebug.log(TAG,"has res : $iconRes")
//                    view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,colorRes))
//
////                    view.clearColorFilter()
////                    view.setColorFilter(ContextCompat.getColor(context,colorRes))
//                }else{

//                    AppDebug.log(TAG,"new res : $iconRes")
                    view.setImageResource(iconRes)
//                    view.tag=iconRes
//                    view.clearColorFilter()
                    view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,colorRes))
//                    view.setColorFilter(ContextCompat.getColor(context,colorRes))
//                }


//
//                view.setImageResource(iconRes)
//
//                view.setColorFilter(ContextCompat.getColor(context,colorRes))



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun changeSvgDrawableColor(context: Context, colorRes: Int, view: ImageView) {
            try {
                view.setColorFilter(
                    ContextCompat.getColor(context, colorRes),
                    PorterDuff.Mode.SRC_IN
                )


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    }

}
