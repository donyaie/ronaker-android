package com.savvi.rangedatepicker

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import java.text.DateFormat
import java.text.NumberFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class MonthView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    internal lateinit var title: TextView
    internal lateinit var grid: CalendarGridView
    private var listener: Listener? = null
    var decorators: List<CalendarCellDecorator>? = null
    private var isRtl: Boolean = false
    private lateinit var  locale: Locale

    internal var deactivatedDates: ArrayList<Int>? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        title = findViewById(R.id.title)
        grid = findViewById(R.id.calendar_grid)
    }

    fun init(month: MonthDescriptor, cells: List<List<MonthCellDescriptor>>,
             displayOnly: Boolean, titleTypeface: Typeface?, dateTypeface: Typeface?, deactivatedDates: ArrayList<Int>, subTitles: ArrayList<SubTitle>?) {


        Logr.d("Initializing MonthView (%d) for %s", System.identityHashCode(this), month)
        val start = System.currentTimeMillis()
        title.text = month.label
        val numberFormatter = NumberFormat.getInstance(locale)

        val numRows = cells.size
        grid.setNumRows(numRows)
        for (i in 0..5) {
            val weekRow = grid.getChildAt(i + 1) as CalendarRowView
            weekRow.setListener(listener)
            if (i < numRows) {
                weekRow.visibility = View.VISIBLE
                val week = cells[i]
                for (c in week.indices) {
                    val cell = week[if (isRtl) 6 - c else c]
                    val cellView = weekRow.getChildAt(c) as CalendarCellView

                    val cellDate = numberFormatter.format(cell.value.toLong())

                    if (cellView.dayOfMonthTextView?.text != cellDate) {
                        cellView.dayOfMonthTextView?.text = cellDate
                    }

                    val subTitle = SubTitle.getByDate(subTitles, cell.date)

                    if (subTitle != null && cellView.subTitleTextView?.text != subTitle.title) {
                        cellView.subTitleTextView?.text = subTitle.title
                    }

                    cellView.isEnabled = cell.isCurrentMonth
                    val dayOfWeek = c + 1
                    if (deactivatedDates.contains(dayOfWeek))
                        cellView.isClickable = false
                    else
                        cellView.isClickable = !displayOnly


                    if (deactivatedDates.contains(dayOfWeek)) {
                        cellView.isSelectable = cell.isSelectable
                        cellView.isSelected = false
                        cellView.isCurrentMonth = cell.isCurrentMonth
                        cellView.isToday = cell.isToday
                        cellView.rangeState = cell.rangeState!!
                        cellView.isHighlighted = cell.isHighlighted
                        cellView.setRangeUnavailable(cell.isUnavailable)
                        cellView.setDeactivated(true)

                    } else {

                        cellView.isSelectable = cell.isSelectable
                        cellView.isSelected = cell.isSelected
                        cellView.isCurrentMonth = cell.isCurrentMonth
                        cellView.isToday = cell.isToday
                        cellView.rangeState = cell.rangeState!!
                        cellView.isHighlighted = cell.isHighlighted
                        cellView.setRangeUnavailable(cell.isUnavailable)
                        cellView.setDeactivated(false)
                    }


                    cellView.tag = cell

                    if (null != decorators) {
                        for (decorator in decorators!!) {
                            decorator.decorate(cellView, cell.date)
                        }
                    }
                }
            } else {
                weekRow.visibility = View.GONE
            }
        }

        if (titleTypeface != null) {
            title.typeface = titleTypeface
        }
        if (dateTypeface != null) {
            grid.setTypeface(dateTypeface)
        }

        Logr.d("MonthView.init took %d ms", System.currentTimeMillis() - start)
    }

    fun setDividerColor(color: Int) {
        grid.setDividerColor(color)
    }

    fun setDayBackground(resId: Int) {
        grid.setDayBackground(resId)
    }

    fun setDayTextColor(resId: Int) {
        grid.setDayTextColor(resId)
    }

    fun setDayViewAdapter(adapter: DayViewAdapter) {
        grid.setDayViewAdapter(adapter)
    }

    fun setTitleTextColor(color: Int) {
        title.setTextColor(color)
    }

    fun setDisplayHeader(displayHeader: Boolean) {
        grid.setDisplayHeader(displayHeader)
    }

    fun setHeaderTextColor(color: Int) {
        grid.setHeaderTextColor(color)
    }

    interface Listener {
        fun handleClick(cell: MonthCellDescriptor)
    }

    companion object {

        fun create(parent: ViewGroup, inflater: LayoutInflater,
                   weekdayNameFormat: DateFormat, listener: Listener, today: Calendar, dividerColor: Int,
                   dayBackgroundResId: Int, dayTextColorResId: Int, titleTextColor: Int, displayHeader: Boolean,
                   headerTextColor: Int, locale: Locale, adapter: DayViewAdapter): MonthView {
            return create(parent, inflater, weekdayNameFormat, listener, today, dividerColor,
                    dayBackgroundResId, dayTextColorResId, titleTextColor, displayHeader, headerTextColor, null,
                    locale, adapter)
        }

        fun create(parent: ViewGroup, inflater: LayoutInflater,
                   weekdayNameFormat: DateFormat, listener: Listener, today: Calendar, dividerColor: Int,
                   dayBackgroundResId: Int, dayTextColorResId: Int, titleTextColor: Int, displayHeader: Boolean,
                   headerTextColor: Int, decorators: List<CalendarCellDecorator>?, locale: Locale,
                   adapter: DayViewAdapter): MonthView {
            val view = inflater.inflate(R.layout.month, parent, false) as MonthView
            view.setDayViewAdapter(adapter)
            view.setDividerColor(dividerColor)
            view.setDayTextColor(dayTextColorResId)
            view.setTitleTextColor(titleTextColor)
            view.setDisplayHeader(displayHeader)
            view.setHeaderTextColor(headerTextColor)

            if (dayBackgroundResId != 0) {
                view.setDayBackground(dayBackgroundResId)
            }

            val originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK)

            view.isRtl = isRtl(locale)
            view.locale = locale
            val firstDayOfWeek = today.firstDayOfWeek
            val headerRow = view.grid.getChildAt(0) as CalendarRowView
            for (offset in 0..6) {
                today.set(Calendar.DAY_OF_WEEK, getDayOfWeek(firstDayOfWeek, offset, view.isRtl))
                val textView = headerRow.getChildAt(offset) as TextView
                textView.text = weekdayNameFormat.format(today.time)
            }
            today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek)
            view.listener = listener
            view.decorators = decorators
            return view
        }

        private fun getDayOfWeek(firstDayOfWeek: Int, offset: Int, isRtl: Boolean): Int {
            val dayOfWeek = firstDayOfWeek + offset
            return if (isRtl) {
                8 - dayOfWeek
            } else dayOfWeek
        }

        private fun isRtl(locale: Locale): Boolean {
            val directionality = Character.getDirectionality(locale.getDisplayName(locale)[0]).toInt()
            return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
        }
    }
}
