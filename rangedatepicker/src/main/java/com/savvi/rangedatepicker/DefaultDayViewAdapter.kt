package com.savvi.rangedatepicker

import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.TextView

class DefaultDayViewAdapter : DayViewAdapter {
    override fun makeCellView(parent: CalendarCellView) {

        val subTitleTextView = TextView(
                ContextThemeWrapper(parent.context, R.style.CalendarCell_SubTitle))
        subTitleTextView.isDuplicateParentStateEnabled = true
        subTitleTextView.gravity = Gravity.BOTTOM
        parent.addView(subTitleTextView)

        val textView = TextView(
                ContextThemeWrapper(parent.context, R.style.CalendarCell_CalendarDate))
        textView.isDuplicateParentStateEnabled = true
        parent.addView(textView)

        parent.dayOfMonthTextView = textView
        parent.subTitleTextView = subTitleTextView
    }
}
