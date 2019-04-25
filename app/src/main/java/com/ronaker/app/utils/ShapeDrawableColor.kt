package com.ronaker.app.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageButton
import android.widget.ImageView


class ShapeDrawableHelper {

    // ==== final variables ==== //
    val TAG = ShapeDrawableHelper::class.java.name

    companion object {

        // =============== inner methods ================ //

        // get a colored drawable (resource color)
        fun getColoredDrawableResource(context: Context, gradientDrawableID: Int, resColorID: Int): GradientDrawable {
            val Shape = context.resources.getDrawable(gradientDrawableID) as GradientDrawable
            Shape.mutate()
            Shape.setColor(context.resources.getColor(resColorID))
            return Shape
        }

        // get a colored drawable (int color (Hex color))
        fun getColoredDrawableColor(context: Context, gradientDrawableID: Int, colorID: Int): GradientDrawable {
            val Shape = context.resources.getDrawable(gradientDrawableID) as GradientDrawable
            Shape.mutate()
            Shape.setColor(colorID)
            return Shape
        }

        // set color to an icon-image

        /**
         * @param context
         * @param imageView
         * @param iconID
         * @param colorID
         */
        fun setIconColorRes(context: Context, imageView: ImageView, iconID: Int, colorID: Int) {

            val icon = context.resources.getDrawable(iconID) as BitmapDrawable
            icon.mutate()
            icon.setColorFilter(context.resources.getColor(colorID), android.graphics.PorterDuff.Mode.SRC_IN)
            imageView.setImageDrawable(icon)

        }

        fun setIconSVGColorRes(context: Context, imageView: ImageView, iconID: Int, colorID: Int) {
            val mDrawable = context.resources.getDrawable(iconID)
            mDrawable.colorFilter = PorterDuffColorFilter(context.resources.getColor(colorID), PorterDuff.Mode.MULTIPLY)


            imageView.setImageDrawable(mDrawable)

        }

        // set color to an icon-image
        fun setIconColorRes(context: Context, imageButton: ImageButton, iconID: Int, colorID: Int) {

            val icon = context.resources.getDrawable(iconID) as BitmapDrawable
            icon.mutate()
            icon.setColorFilter(context.resources.getColor(colorID), android.graphics.PorterDuff.Mode.SRC_IN)
            imageButton.setImageDrawable(icon)

        }



        // set color to an icon-image
        fun setIconColor(context: Context, imageView: ImageView, iconID: Int, color: Int) {

            val icon = context.resources.getDrawable(iconID) as BitmapDrawable
            icon.mutate()
            icon.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
            imageView.setImageDrawable(icon)

        }

        // get a colored icon as drawable
        fun getColoredIcon(context: Context, iconID: Int, colorID: Int): BitmapDrawable {

            val icon = context.resources.getDrawable(iconID) as BitmapDrawable
            icon.mutate()
            icon.setColorFilter(context.resources.getColor(colorID), android.graphics.PorterDuff.Mode.SRC_IN)


            return icon

        }

        // get a colored icon as drawable
        fun setDrawableColor(drawable: Drawable, color: Int): Drawable {
            drawable.mutate()
            drawable.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
            return drawable

        }
    }

}
