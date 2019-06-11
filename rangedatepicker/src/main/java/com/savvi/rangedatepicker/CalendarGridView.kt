package com.savvi.rangedatepicker

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.core.content.ContextCompat

/**
 * ViewGroup that draws a grid of calendar cells.  All children must be [CalendarRowView]s.
 * The first row is assumed to be a header and no divider is drawn above it.
 */
class CalendarGridView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private val dividerPaint = Paint()
    private var oldWidthMeasureSize: Int = 0
    private var oldNumRows: Int = 0

    init {
        dividerPaint.color = ContextCompat.getColor(context, R.color.calendar_divider)

    }

    fun setDividerColor(color: Int) {
        dividerPaint.color = color
    }

    fun setDayViewAdapter(adapter: DayViewAdapter) {
        for (i in 0 until childCount) {
            (getChildAt(i) as CalendarRowView).setDayViewAdapter(adapter)
        }
    }

    fun setDayBackground(resId: Int) {
        for (i in 1 until childCount) {
            (getChildAt(i) as CalendarRowView).setCellBackground(resId)
        }
    }

    fun setDayTextColor(resId: Int) {
        for (i in 0 until childCount) {


            val colors = ContextCompat.getColorStateList(context,resId)
            colors?.let { (getChildAt(i) as CalendarRowView).setCellTextColor(it) }
        }
    }

    fun setDisplayHeader(displayHeader: Boolean) {
        getChildAt(0).visibility = if (displayHeader) View.VISIBLE else View.GONE
    }

    fun setHeaderTextColor(color: Int) {
        (getChildAt(0) as CalendarRowView).setCellTextColor(color)
    }

    fun setTypeface(typeface: Typeface) {
        for (i in 0 until childCount) {
            (getChildAt(i) as CalendarRowView).setTypeface(typeface)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (childCount == 0) {
            (child as CalendarRowView).setIsHeaderRow(true)
        }
        super.addView(child, index, params)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val row = getChildAt(1) as ViewGroup
        val top = row.top
        val bottom = bottom
        // Left side border.
        val left = row.getChildAt(0).left + left
        canvas.drawLine(left + FLOAT_FUDGE, top.toFloat(), left + FLOAT_FUDGE, bottom.toFloat(), dividerPaint)

        // Each cell's right-side border.
        for (c in 0..6) {
            val x = left + row.getChildAt(c).right - FLOAT_FUDGE
            canvas.drawLine(x, top.toFloat(), x, bottom.toFloat(), dividerPaint)
        }
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val retVal = super.drawChild(canvas, child, drawingTime)
        // Draw a bottom border.
        val bottom = child.bottom - 1
        canvas.drawLine(child.left.toFloat(), bottom.toFloat(), (child.right - 2).toFloat(), bottom.toFloat(), dividerPaint)
        return retVal
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Logr.d("Grid.onMeasure w=%s h=%s", View.MeasureSpec.toString(widthMeasureSpec),
                View.MeasureSpec.toString(heightMeasureSpec))
        var widthMeasureSize = View.MeasureSpec.getSize(widthMeasureSpec)
        if (oldWidthMeasureSize == widthMeasureSize) {
            Logr.d("SKIP Grid.onMeasure")
            setMeasuredDimension(measuredWidth, measuredHeight)
            return
        }
        val start = System.currentTimeMillis()
        oldWidthMeasureSize = widthMeasureSize
        val cellSize = widthMeasureSize / 7
        // Remove any extra pixels since /7 is unlikely to give whole nums.
        widthMeasureSize = cellSize * 7
        var totalHeight = 0
        val rowWidthSpec = makeMeasureSpec(widthMeasureSize, EXACTLY)
        val rowHeightSpec = makeMeasureSpec(cellSize, EXACTLY)
        var c = 0
        val numChildren = childCount
        while (c < numChildren) {
            val child = getChildAt(c)
            if (child.visibility == View.VISIBLE) {
                if (c == 0) { // It's the header: height should be wrap_content.
                    measureChild(child, rowWidthSpec, makeMeasureSpec(cellSize, AT_MOST))
                } else {
                    measureChild(child, rowWidthSpec, rowHeightSpec)
                }
                totalHeight += child.measuredHeight
            }
            c++
        }
        val measuredWidth = widthMeasureSize + 2 // Fudge factor to make the borders show up.
        setMeasuredDimension(measuredWidth, totalHeight)
        Logr.d("Grid.onMeasure %d ms", System.currentTimeMillis() - start)
    }

    override fun onLayout(changed: Boolean, left: Int, mtop: Int, right: Int, bottom: Int) {
        var top = mtop
        val start = System.currentTimeMillis()
        top = 0
        var c = 0
        val numChildren = childCount
        while (c < numChildren) {
            val child = getChildAt(c)
            val rowHeight = child.measuredHeight
            child.layout(left, top, right, top + rowHeight)
            top += rowHeight
            c++
        }
        Logr.d("Grid.onLayout %d ms", System.currentTimeMillis() - start)
    }

    fun setNumRows(numRows: Int) {
        if (oldNumRows != numRows) {
            // If the number of rows changes, make sure we do a re-measure next time around.
            oldWidthMeasureSize = 0
        }
        oldNumRows = numRows
    }

    companion object {
        /**
         * The grid lines don't exactly line up on certain devices (Nexus 7, Nexus 5). Fudging the
         * co-ordinates by half a point seems to fix this without breaking other devices.
         */
        private val FLOAT_FUDGE = 0.5f
    }
}
