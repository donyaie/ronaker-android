package com.savvi.rangedatepicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class CalendarCellView(context: Context, attrs: AttributeSet)//
    : FrameLayout(context, attrs) {

    var isSelectable = false
        set(isSelectable) {
            if (this.isSelectable != isSelectable) {
                field = isSelectable
                refreshDrawableState()
            }
        }
    var isCurrentMonth = false
        set(isCurrentMonth) {
            if (this.isCurrentMonth != isCurrentMonth) {
                field = isCurrentMonth
                refreshDrawableState()
            }
        }
    var isToday = false
        set(isToday) {
            if (this.isToday != isToday) {
                field = isToday
                refreshDrawableState()
            }
        }
    var isHighlighted = false
        set(isHighlighted) {
            if (this.isHighlighted != isHighlighted) {
                field = isHighlighted
                refreshDrawableState()
            }
        }
    private var isAvailable = false
    private var isDeactivated = false
    var rangeState = RangeState.NONE
        set(rangeState) {
            if (this.rangeState != rangeState) {
                field = rangeState
                refreshDrawableState()
            }
        }
    //textView.setTextSize(8);
    var dayOfMonthTextView: TextView? = null
        get() {
            if (field == null) {
                throw IllegalStateException(
                        "You have to setDayOfMonthTextView in your custom DayViewAdapter."
                )
            }
            return field
        }
    var subTitleTextView: TextView? = null
        get() {
            if (field == null) {
                throw IllegalStateException(
                        "You have to setSubTitleTextView in your custom DayViewAdapter."
                )
            }
            return field
        }

    fun setRangeUnavailable(isAvailable: Boolean) {
        if (this.isAvailable != isAvailable) {
            this.isAvailable = isAvailable
            refreshDrawableState()
        }
    }

    fun setDeactivated(isDeactivated: Boolean) {
        if (this.isDeactivated != isDeactivated) {
            this.isDeactivated = isDeactivated
            refreshDrawableState()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 5)

        if (this.isSelectable) {
            View.mergeDrawableStates(drawableState, STATE_SELECTABLE)
        }

        if (this.isCurrentMonth) {
            View.mergeDrawableStates(drawableState, STATE_CURRENT_MONTH)
        }

        if (this.isToday) {
            View.mergeDrawableStates(drawableState, STATE_TODAY)
        }

        if (this.isHighlighted) {
            View.mergeDrawableStates(drawableState, STATE_HIGHLIGHTED)
        }

        if (isAvailable) {
            View.mergeDrawableStates(drawableState, STATE_UNAVAILABLE)
        }

        if (isDeactivated) {
            View.mergeDrawableStates(drawableState, STATE_DEACTIVATED)
        }

        if (this.rangeState == RangeState.FIRST) {
            View.mergeDrawableStates(drawableState, STATE_RANGE_FIRST)
        } else if (this.rangeState == RangeState.MIDDLE) {
            View.mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE)
        } else if (this.rangeState == RangeState.LAST) {
            View.mergeDrawableStates(drawableState, STATE_RANGE_LAST)
        }

        return drawableState
    }

    companion object {
        private val STATE_SELECTABLE = intArrayOf(R.attr.tsquare_state_selectable)
        private val STATE_CURRENT_MONTH = intArrayOf(R.attr.tsquare_state_current_month)
        private val STATE_TODAY = intArrayOf(R.attr.tsquare_state_today)
        private val STATE_HIGHLIGHTED = intArrayOf(R.attr.tsquare_state_highlighted)
        private val STATE_RANGE_FIRST = intArrayOf(R.attr.tsquare_state_range_first)
        private val STATE_RANGE_MIDDLE = intArrayOf(R.attr.tsquare_state_range_middle)
        private val STATE_RANGE_LAST = intArrayOf(R.attr.tsquare_state_range_last)
        private val STATE_UNAVAILABLE = intArrayOf(R.attr.tsquare_state_unavailable)

        private val STATE_DEACTIVATED = intArrayOf(R.attr.tsquare_state_deactivated)
    }
}
