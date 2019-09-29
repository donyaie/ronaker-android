package com.ronaker.app.utils.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager


class ViewPagerCustom : ViewPager {



    private var isPagingEnabled = false

    private var mScroller: ScrollerCustomDuration? = null

    constructor(context: Context) : super(context) {


        postInitViewPager()


    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        postInitViewPager()
    }

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private fun postInitViewPager() {
        try {


            val scroller = ViewPager::class.java.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller = ScrollerCustomDuration(
                context,
                interpolator.get(null) as Interpolator
            )
            scroller.set(this, mScroller)


        } catch (e: Exception) {
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(b: Boolean) {
        this.isPagingEnabled = b
    }

    /**
     * Set the factor by which the duration will change
     */
    fun setScrollDurationFactor(scrollFactor: Double) {
        mScroller?.setScrollDurationFactor(scrollFactor)
    }


    class ScrollerCustomDuration : Scroller {

        private var mScrollFactor = 1.0

        constructor(context: Context) : super(context) {}

        constructor(context: Context, interpolator: Interpolator) : super(context, interpolator) {}

        @SuppressLint("NewApi")
        constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(
            context,
            interpolator,
            flywheel
        ) {
        }

        /**
         * Set the factor by which the duration will change
         */
        fun setScrollDurationFactor(scrollFactor: Double) {
            mScrollFactor = scrollFactor
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, (duration * mScrollFactor).toInt())
        }

    }
}