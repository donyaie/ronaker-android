package com.ronaker.app.utils.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class StatusBarSizeView : RelativeLayout {

    companion object {

        // status bar saved size
        var heightSize: Int = 0
    }

    constructor(context: Context) :
            super(context) {
        this.init()
    }

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) {
        this.init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        this.init()
    }

    private fun init() {

        // do nothing if we already have the size
        if (heightSize != 0) {
            return
        }

//        var sc= ScreenCalcute(context)
//
//        heightSize= sc.DP2Pixel(40)

//        heightSize=context.resources.getDimension(R.dimen.statusBarSize).toInt()
//
//        // listen to get the height
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            (context as? Activity)?.window?.decorView?.setOnApplyWindowInsetsListener { _, windowInsets ->
//
//                // get the size
//                heightSize = windowInsets.systemWindowInsetTop
//
//
//
//                // return insets
//                windowInsets
//            }
//        }


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // if height is not zero height is ok
        if (h != 0 || heightSize == 0) {
            return
        }

        // apply the size
        postDelayed({
            applyHeight(heightSize)
        }, 0)
    }

    private fun applyHeight(height: Int) {

        // apply the status bar height to the height of the view
        val lp = this.layoutParams
        lp.height = height
        this.layoutParams = lp
    }

}