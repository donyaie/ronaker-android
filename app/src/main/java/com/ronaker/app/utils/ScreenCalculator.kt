package com.ronaker.app.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import kotlin.math.pow
import kotlin.math.sqrt


class ScreenCalculator(context: Context) {

    private val TAG = ScreenCalculator::class.java.name

    private var display: Display
    private var displayMetrics: DisplayMetrics

    // ====== Inner Methods ====== //

     val screenDensity: Float
        get() = displayMetrics.density

     val screenDPI: Int
        get() = displayMetrics.densityDpi

     val screenHeightPixel: Float
        get() {
            display.getMetrics(displayMetrics)
            return displayMetrics.heightPixels.toFloat()
        }

     val screenWidthPixel: Float
        get() {
            display.getMetrics(displayMetrics)
            return displayMetrics.widthPixels.toFloat()
        }

     val screenHeightDP: Float
        get() = screenHeightPixel / screenDensity

     val screenWidthDP: Float
        get() = screenWidthPixel / screenDensity

     val screenInch: Float
        get() =
            sqrt(
                (screenWidthDP / 160).toDouble().pow(2.0) + (screenHeightDP / 160).toDouble().pow(
                    2.0
                )
            ).toFloat()

     val aspectRation: Float
        get() = screenHeightDP / screenWidthDP

     val rotation: Int
        get() = display.rotation

     val isLandScape: Boolean
        get() {
            val rotation = rotation
            return rotation != Surface.ROTATION_0 && rotation != Surface.ROTATION_180

        }

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.display = wm.defaultDisplay
        this.displayMetrics = context.resources.displayMetrics

    }

    fun Pixel2DP(Pixel: Float): Float {
        return Pixel / screenDensity

    }

    fun DP2Pixel(DP: Int): Int {
        return (screenDensity * DP).toInt()
    }
}
