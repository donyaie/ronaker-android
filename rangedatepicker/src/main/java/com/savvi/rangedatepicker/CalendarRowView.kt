package com.savvi.rangedatepicker

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec

/** TableRow that draws a divider between each cell. To be used with [CalendarGridView].  */
class CalendarRowView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs), View.OnClickListener {
    private var isHeaderRow: Boolean = false
    private var listener: MonthView.Listener? = null

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        child.setOnClickListener(this)
        super.addView(child, index, params)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val start = System.currentTimeMillis()
        val totalWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        var rowHeight = 0
        var c = 0
        val numChildren = childCount
        while (c < numChildren) {
            val child = getChildAt(c)
            // Calculate width cells, making sure to cover totalWidth.
            val l = (c + 0) * totalWidth / 7
            val r = (c + 1) * totalWidth / 7
            val cellSize = r - l
            val cellWidthSpec = makeMeasureSpec(cellSize, EXACTLY)
            val cellHeightSpec = if (isHeaderRow) makeMeasureSpec(cellSize, AT_MOST) else cellWidthSpec
            child.measure(cellWidthSpec, cellHeightSpec)
            // The row height is the height of the tallest cell.
            if (child.measuredHeight > rowHeight) {
                rowHeight = child.measuredHeight
            }
            c++
        }
        val widthWithPadding = totalWidth + paddingLeft + paddingRight
        val heightWithPadding = rowHeight + paddingTop + paddingBottom
        setMeasuredDimension(widthWithPadding, heightWithPadding)
        Logr.d("Row.onMeasure %d ms", System.currentTimeMillis() - start)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val start = System.currentTimeMillis()
        val cellHeight = bottom - top
        val width = right - left
        var c = 0
        val numChildren = childCount
        while (c < numChildren) {
            val child = getChildAt(c)
            val l = (c + 0) * width / 7
            val r = (c + 1) * width / 7
            child.layout(l, 0, r, cellHeight)
            c++
        }
        Logr.d("Row.onLayout %d ms", System.currentTimeMillis() - start)
    }

    fun setIsHeaderRow(isHeaderRow: Boolean) {
        this.isHeaderRow = isHeaderRow
    }

    override fun onClick(v: View) {
        // Header rows don't have a click listener
        if (listener != null) {
            listener!!.handleClick(v.tag as MonthCellDescriptor)
        }
    }

    fun setListener(listener: MonthView.Listener?) {
        this.listener = listener
    }

    fun setDayViewAdapter(adapter: DayViewAdapter) {
        for (i in 0 until childCount) {
            if (getChildAt(i) is CalendarCellView) {
                val cell = getChildAt(i) as CalendarCellView
                cell.removeAllViews()
                adapter.makeCellView(cell)
            }
        }
    }

    fun setCellBackground(resId: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).setBackgroundResource(resId)
        }
    }

    fun setCellTextColor(resId: Int) {
        for (i in 0 until childCount) {
            if (getChildAt(i) is CalendarCellView) {
                (getChildAt(i) as CalendarCellView).dayOfMonthTextView?.setTextColor(resId)
            } else {
                (getChildAt(i) as TextView).setTextColor(resId)
            }
        }
    }

    fun setCellTextColor(colors: ColorStateList) {
        for (i in 0 until childCount) {
            if (getChildAt(i) is CalendarCellView) {
                (getChildAt(i) as CalendarCellView).dayOfMonthTextView?.setTextColor(colors)
            } else {
                (getChildAt(i) as TextView).setTextColor(colors)
            }
        }
    }

    fun setTypeface(typeface: Typeface) {
        for (i in 0 until childCount) {
            if (getChildAt(i) is CalendarCellView) {
                (getChildAt(i) as CalendarCellView).dayOfMonthTextView?.typeface = typeface
            } else {
                (getChildAt(i) as TextView).typeface = typeface
            }
        }
    }
}
