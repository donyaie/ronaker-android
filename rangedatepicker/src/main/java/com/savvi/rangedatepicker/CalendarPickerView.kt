package com.savvi.rangedatepicker

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.TimeZone

import java.util.Calendar.DATE
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.DAY_OF_WEEK
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MILLISECOND
import java.util.Calendar.MINUTE
import java.util.Calendar.MONTH
import java.util.Calendar.SECOND
import java.util.Calendar.YEAR
import kotlin.collections.ArrayList


class CalendarPickerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var subTitles: ArrayList<SubTitle>? = null

    private val adapter: CalendarPickerView.MonthAdapter?
//    private val layoutManager: LayoutManager
    private val cells = IndexedLinkedHashMap<String, List<List<MonthCellDescriptor>>>()
    internal val listener: MonthView.Listener = CellClickedListener()
    internal val months: MutableList<MonthDescriptor> = ArrayList()
    internal val selectedCells: MutableList<MonthCellDescriptor> = ArrayList()
    internal val highlightedCells: MutableList<MonthCellDescriptor> = ArrayList()
    internal val selectedCals: MutableList<Calendar> = ArrayList()
    internal val highlightedCals: MutableList<Calendar> = ArrayList()
    internal var deactivatedDates = ArrayList<Int>()
    private var locale: Locale? = null
    private var timeZone: TimeZone? = null
    private var monthNameFormat: DateFormat? = null
    private var weekdayNameFormat: DateFormat? = null
    private var fullDateFormat: DateFormat? = null
    private var minCal: Calendar? = null
    private var maxCal: Calendar? = null
    private var monthCounter: Calendar? = null
    private var displayOnly: Boolean = false
    internal lateinit var selectionMode: SelectionMode
    internal lateinit var today: Calendar
    private val dividerColor: Int
    private val dayBackgroundResId: Int
    private val dayTextColorResId: Int
    private val titleTextColor: Int
    private val displayHeader: Boolean
    private val orientation: Boolean
    private val headerTextColor: Int
    private var titleTypeface: Typeface? = null
    private var dateTypeface: Typeface? = null

    private var dateListener: OnDateSelectedListener? = null
    private var dateConfiguredListener: DateSelectableFilter? = null
    private var invalidDateListener: OnInvalidDateSelectedListener? = DefaultOnInvalidDateSelectedListener()
    private var cellClickInterceptor: CellClickInterceptor? = null
   public var decorators: List<CalendarCellDecorator>? = null
        set(decorators) {
            field = decorators
            adapter?.notifyDataSetChanged()
        }
    private var dayViewAdapter: DayViewAdapter = DefaultDayViewAdapter()

    private var monthsReverseOrder = false

    val selectedDate: Date?
        get() = if (selectedCals.size > 0) selectedCals[0].time else null

    val selectedDates: List<Date>
        get() {
            val selectedDates = ArrayList<Date>()
            for (cal in selectedCells) {
                if (!highlightedCells.contains(cal) && !deactivatedDates.contains(cal.date.day + 1))
                    selectedDates.add(cal.date)
            }
            Collections.sort(selectedDates)
            return selectedDates
        }

    enum class SelectionMode {
        /**
         * Only one date will be selectable.  If there is already a selected date and you select a new
         * one, the old date will be unselected.
         */
        SINGLE,
        /**
         * Multiple dates will be selectable.  Selecting an already-selected date will un-select it.
         */
        MULTIPLE,
        /**
         * Allows you to select a date range.  Previous selections are cleared when you either:
         *
         *  * Have a range selected and select another date (even if it's in the current range).
         *  * Have one date selected and then select an earlier date.
         *
         */
        RANGE
    }

    init {

        val res = context.resources
        val a = context.obtainStyledAttributes(attrs, R.styleable.CalendarPickerView)
        val bg = a.getColor(R.styleable.CalendarPickerView_android_background,
                 ContextCompat.getColor(context, R.color.calendar_bg)   )
        dividerColor = a.getColor(R.styleable.CalendarPickerView_tsquare_dividerColor,
            ContextCompat.getColor(context,R.color.calendar_divider))
        dayBackgroundResId = a.getResourceId(R.styleable.CalendarPickerView_tsquare_dayBackground,
                R.drawable.calendar_bg_selector)
        dayTextColorResId = a.getResourceId(R.styleable.CalendarPickerView_tsquare_dayTextColor,
                R.drawable.day_text_color)
        titleTextColor = a.getColor(R.styleable.CalendarPickerView_tsquare_titleTextColor,
            ContextCompat.getColor(context,R.color.dateTimeRangePickerTitleTextColor))
        displayHeader = a.getBoolean(R.styleable.CalendarPickerView_tsquare_displayHeader, true)
        headerTextColor = a.getColor(R.styleable.CalendarPickerView_tsquare_headerTextColor,
            ContextCompat.getColor(context,R.color.dateTimeRangePickerHeaderTextColor))
        orientation = a.getBoolean(R.styleable.CalendarPickerView_tsquare_orientation_horizontal, false)

        a.recycle()


        adapter = MonthAdapter()
        if (!orientation) {
            layoutManager = LinearLayoutManager(getContext(), RecyclerView.VERTICAL, monthsReverseOrder)
        } else {
            layoutManager = LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, monthsReverseOrder)
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }

        setLayoutManager(layoutManager)
        setBackgroundColor(bg)
        timeZone = TimeZone.getDefault()
        locale = Locale.getDefault()

        if (isInEditMode) {
            val nextYear = Calendar.getInstance(timeZone, locale)
            nextYear.add(Calendar.YEAR, 1)

            init(Date(), nextYear.time) //
                    .withSelectedDate(Date())
        }
    }

    /**
     * Both date parameters must be non-null and their [Date.getTime] must not return 0. Time
     * of day will be ignored.  For instance, if you pass in `minDate` as 11/16/2012 5:15pm and
     * `maxDate` as 11/16/2013 4:30am, 11/16/2012 will be the first selectable date and
     * 11/15/2013 will be the last selectable date (`maxDate` is exclusive).
     *
     *
     * This will implicitly set the [SelectionMode] to [SelectionMode.SINGLE].  If you
     * want a different selection mode, use [FluentInitializer.inMode] on the
     * [FluentInitializer] this method returns.
     *
     *
     * The calendar will be constructed using the given time zone and the given locale. This means
     * that all dates will be in given time zone, all names (months, days) will be in the language
     * of the locale and the weeks start with the day specified by the locale.
     *
     * @param minDate Earliest selectable date, inclusive.  Must be earlier than `maxDate`.
     * @param maxDate Latest selectable date, exclusive.  Must be later than `minDate`.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    fun init(minDate: Date?, maxDate: Date?, timeZone: TimeZone?, locale: Locale?, monthNameFormat: DateFormat): FluentInitializer {

        if (minDate == null || maxDate == null) {
            throw IllegalArgumentException(
                    "minDate and maxDate must be non-null.  " + dbg(minDate, maxDate))
        }
        if (minDate.after(maxDate)) {
            throw IllegalArgumentException(
                    "minDate must be before maxDate.  " + dbg(minDate, maxDate))
        }
        if (locale == null) {
            throw IllegalArgumentException("Locale is null.")
        }
        if (timeZone == null) {
            throw IllegalArgumentException("Time zone is null.")
        }

        // Make sure that all calendar instances use the same time zone and locale.
        this.timeZone = timeZone
        this.locale = locale

        today = Calendar.getInstance(timeZone, locale)
        minCal = Calendar.getInstance(timeZone, locale)
        maxCal = Calendar.getInstance(timeZone, locale)
        monthCounter = Calendar.getInstance(timeZone, locale)
        this.monthNameFormat = monthNameFormat
        monthNameFormat.timeZone = timeZone
        weekdayNameFormat = SimpleDateFormat("E", locale)
        weekdayNameFormat!!.timeZone = timeZone
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
        fullDateFormat!!.timeZone = timeZone

        this.selectionMode = SelectionMode.SINGLE
        // Clear out any previously-selected dates/cells.
        selectedCals.clear()
        selectedCells.clear()
        highlightedCals.clear()
        highlightedCells.clear()

        // Clear previous state.
        cells.clear()
        months.clear()
        minCal!!.time = minDate
        maxCal!!.time = maxDate
        setMidnight(minCal!!)
        setMidnight(maxCal!!)
        displayOnly = false

        // maxDate is exclusive: bump back to the previous day so if maxDate is the first of a month,
        // we don't accidentally include that month in the view.
        maxCal!!.add(MILLISECOND, -1)

        // Now iterate between minCal and maxCal and build up our list of months to show.
        monthCounter!!.time = minCal!!.time
        val maxMonth = maxCal!!.get(MONTH)
        val maxYear = maxCal!!.get(YEAR)
        while ((monthCounter!!.get(MONTH) <= maxMonth // Up to, including the month.
                        || monthCounter!!.get(YEAR) < maxYear) // Up to the year.
                && monthCounter!!.get(YEAR) < maxYear + 1) { // But not > next yr.
            val date = monthCounter!!.time
            val month = MonthDescriptor(monthCounter!!.get(MONTH), monthCounter!!.get(YEAR), date,
                    monthNameFormat.format(date))
            cells[monthKey(month)] = getMonthCells(month, monthCounter!!)
            Logr.d("Adding month %s", month)
            months.add(month)
            monthCounter!!.add(MONTH, 1)
        }

        validateAndUpdate()
        return FluentInitializer()
    }

    fun init(minDate: Date, maxDate: Date): FluentInitializer {
        return init(minDate, maxDate, TimeZone.getDefault(), Locale.getDefault(), SimpleDateFormat("MMMM", Locale.getDefault()))
    }


    fun init(minDate: Date, maxDate: Date, timeZone: TimeZone): FluentInitializer {
        return init(minDate, maxDate, timeZone, Locale.getDefault(), SimpleDateFormat("MMMM", Locale.getDefault()))
    }

    fun init(minDate: Date, maxDate: Date, monthNameFormat: DateFormat): FluentInitializer {
        return init(minDate, maxDate, TimeZone.getDefault(), Locale.getDefault(), monthNameFormat)
    }


    fun init(minDate: Date, maxDate: Date, locale: Locale): FluentInitializer {
        return init(minDate, maxDate, TimeZone.getDefault(), locale, SimpleDateFormat("MMMM", locale))
    }

    inner class FluentInitializer {


        fun inMode(mode: SelectionMode): FluentInitializer {
            selectionMode = mode
            validateAndUpdate()
            return this
        }

        fun withSelectedDate(selectedDates: Date): FluentInitializer {
            return withSelectedDates(listOf<Date>(selectedDates))
        }


        fun withSelectedDates(selectedDates: Collection<Date>?): FluentInitializer {
            if (selectionMode == SelectionMode.SINGLE && selectedDates!!.size > 1) {
                throw IllegalArgumentException("SINGLE mode can't be used with multiple selectedDates")
            }
            if (selectionMode == SelectionMode.RANGE && selectedDates!!.size > 2) {
                throw IllegalArgumentException(
                        "RANGE mode only allows two selectedDates.  You tried to pass " + selectedDates.size)
            }
            if (selectedDates != null) {
                for (date in selectedDates) {
                    selectDate(date)
                }
            }
            scrollToSelectedDates()

            validateAndUpdate()
            return this
        }

        fun withHighlightedDates(dates: List<Date>): FluentInitializer {
            highlightDates(dates)
            return this
        }


        fun withHighlightedDate(date: Date): FluentInitializer {
            return withHighlightedDates(listOf<Date>(date))
        }

        @SuppressLint("SimpleDateFormat")
        fun setShortWeekdays(newShortWeekdays: Array<String>): FluentInitializer {
            val symbols = DateFormatSymbols(locale)
            symbols.shortWeekdays = newShortWeekdays
            weekdayNameFormat = SimpleDateFormat("E", symbols)
            return this
        }

        fun displayOnly(): FluentInitializer {
            displayOnly = true
            return this
        }

        fun withMonthsReverseOrder(monthsRevOrder: Boolean): FluentInitializer {
            monthsReverseOrder = monthsRevOrder
            return this
        }

        fun withDeactivateDates(deactivateDates: ArrayList<Int>): FluentInitializer {
            deactivateDates(deactivateDates)
            return this
        }

        fun withSubTitles(subTitles: ArrayList<SubTitle>): FluentInitializer {
            setSubTitles(subTitles)
            return this
        }
    }

    private fun setSubTitles(subTitles: ArrayList<SubTitle>) {
        this.subTitles = subTitles
        validateAndUpdate()
    }

    private fun validateAndUpdate() {
        if (getAdapter() == null) {
            setAdapter(adapter)
        }
        adapter!!.notifyDataSetChanged()
    }

    private fun scrollToSelectedMonth(selectedIndex: Int, smoothScroll: Boolean = false) {
        post {
            Logr.d("Scrolling to position %d", selectedIndex)

            if (smoothScroll) {
                smoothScrollToPosition(selectedIndex)
            } else {
                scrollToPosition(selectedIndex)
            }
        }
    }

    private fun scrollToSelectedDates() {
        var selectedIndex: Int? = null
        var todayIndex: Int? = null
        val today = Calendar.getInstance(timeZone, locale)
        for (c in months.indices) {
            val month = months[c]
            if (selectedIndex == null) {
                for (selectedCal in selectedCals) {
                    if (sameMonth(selectedCal, month)) {
                        selectedIndex = c
                        break
                    }
                }
                if (selectedIndex == null && todayIndex == null && sameMonth(today, month)) {
                    todayIndex = c
                }
            }
        }
        if (selectedIndex != null) {
            scrollToSelectedMonth(selectedIndex)
        } else if (todayIndex != null) {
            scrollToSelectedMonth(todayIndex)
        }
    }

    fun scrollToDate(date: Date): Boolean {
        var selectedIndex: Int? = null

        val cal = Calendar.getInstance(timeZone, locale)
        cal.time = date
        for (c in months.indices) {
            val month = months[c]
            if (sameMonth(cal, month)) {
                selectedIndex = c
                break
            }
        }
        if (selectedIndex != null) {
            scrollToSelectedMonth(selectedIndex)
            return true
        }
        return false
    }

    /**
     * This method should only be called if the calendar is contained in a dialog, and it should only
     * be called once, right after the dialog is shown (using
     * [android.content.DialogInterface.OnShowListener] or
     * [android.app.DialogFragment.onStart]).
     */
    fun fixDialogDimens() {
        Logr.d("Fixing dimensions to h = %d / w = %d", measuredHeight, measuredWidth)
        // Fix the layout height/width after the dialog has been shown.
        layoutParams.height = measuredHeight
        layoutParams.width = measuredWidth
        // Post this runnable so it runs _after_ the dimen changes have been applied/re-measured.
        post {
            Logr.d("Dimens are fixed: now scroll to the selected date")
            scrollToSelectedDates()
        }
    }

    /**
     * Set the typeface to be used for month titles.
     */
    fun setTitleTypeface(titleTypeface: Typeface) {
        this.titleTypeface = titleTypeface
        validateAndUpdate()
    }

    /**
     * Sets the typeface to be used within the date grid.
     */
    fun setDateTypeface(dateTypeface: Typeface) {
        this.dateTypeface = dateTypeface
        validateAndUpdate()
    }

    /**
     * Sets the typeface to be used for all text within this calendar.
     */
    fun setTypeface(typeface: Typeface) {
        setTitleTypeface(typeface)
        setDateTypeface(typeface)
    }

    /**
     * This method should only be called if the calendar is contained in a dialog, and it should only
     * be called when the screen has been rotated and the dialog should be re-measured.
     */
    fun unfixDialogDimens() {
        Logr.d("Reset the fixed dimensions to allow for re-measurement")
        // Fix the layout height/width after the dialog has been shown.
        layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (months.isEmpty()) {
            throw IllegalStateException(
                    "Must have at least one month to display.  Did you forget to call init()?")
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private inner class CellClickedListener : MonthView.Listener {
        override fun handleClick(cell: MonthCellDescriptor) {
            val clickedDate = cell.date

            if (highlightedCells.contains(cell)) {
                return
            }

            val calendar = Calendar.getInstance()
            calendar.time = clickedDate

            val day = calendar.get(DAY_OF_WEEK)
            if (deactivatedDates.contains(day)) {
                return
            }

            if (cellClickInterceptor != null && cellClickInterceptor!!.onCellClicked(clickedDate)) {
                return
            }
            if (!betweenDates(clickedDate, minCal!!, maxCal) || !isDateSelectable(clickedDate)) {
                if (invalidDateListener != null) {
                    invalidDateListener!!.onInvalidDateSelected(clickedDate)
                }
            } else {
                val wasSelected = doSelectDate(clickedDate, cell)

                if (dateListener != null) {
                    if (wasSelected) {
                        dateListener!!.onDateSelected(clickedDate)
                    } else {
                        dateListener!!.onDateUnselected(clickedDate)
                    }
                }
            }
        }
    }


    @JvmOverloads
    fun selectDate(date: Date, smoothScroll: Boolean = false): Boolean {
        validateDate(date)

        val monthCellWithMonthIndex = getMonthCellWithIndexByDate(date)
        if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
            return false
        }
        val wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell)
        if (wasSelected) {
            scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex, smoothScroll)
        }
        return wasSelected
    }

    private fun validateDate(date: Date?) {
        if (date == null) {
            throw IllegalArgumentException("Selected date must be non-null.")
        }
        if (date.before(minCal!!.time) || date.after(maxCal!!.time)) {
            throw IllegalArgumentException(String.format(
                    "SelectedDate must be between minDate and maxDate." + "%nminDate: %s%nmaxDate: %s%nselectedDate: %s", minCal!!.time, maxCal!!.time,
                    date))
        }
    }

    private fun doSelectDate(date: Date?, cell: MonthCellDescriptor): Boolean {
        var date = date
        val newlySelectedCal = Calendar.getInstance(timeZone, locale)
        newlySelectedCal.time = date
        // Sanitize input: clear out the hours/minutes/seconds/millis.
        setMidnight(newlySelectedCal)

        // Clear any remaining range state.
        for (selectedCell in selectedCells) {
            selectedCell.rangeState = RangeState.NONE
        }

        when (selectionMode) {
            CalendarPickerView.SelectionMode.RANGE -> if (selectedCals.size > 1) {
                // We've already got a range selected: clear the old one.
                clearOldSelections()
            } else if (selectedCals.size == 1 && newlySelectedCal.before(selectedCals[0])) {
                // We're moving the start of the range back in time: clear the old start date.
                clearOldSelections()
            }

            CalendarPickerView.SelectionMode.MULTIPLE -> date = applyMultiSelect(date, newlySelectedCal)

            CalendarPickerView.SelectionMode.SINGLE -> clearOldSelections()
            else -> throw IllegalStateException("Unknown selectionMode $selectionMode")
        }

        if (date != null) {
            // Select a new cell.
            if (selectedCells.size == 0 || selectedCells[0] != cell) {
                selectedCells.add(cell)
                cell.isSelected = true
            }
            selectedCals.add(newlySelectedCal)

            if (selectionMode == SelectionMode.RANGE && selectedCells.size > 1) {
                // Select all days in between start and end.
                val start = selectedCells[0].date
                val end = selectedCells[1].date
                selectedCells[0].rangeState = RangeState.FIRST
                selectedCells[1].rangeState = RangeState.LAST

                val startMonthIndex = cells.getIndexOfKey(monthKey(selectedCals[0]))
                val endMonthIndex = cells.getIndexOfKey(monthKey(selectedCals[1]))
                for (monthIndex in startMonthIndex..endMonthIndex) {
                    val month = cells.getValueAtIndex(monthIndex)
                    for (week in month!!) {
                        for (singleCell in week) {
                            if (singleCell.date.after(start)
                                    && singleCell.date.before(end)
                                    && singleCell.isSelectable) {
                                if (highlightedCells.contains(singleCell)) {
                                    singleCell.isSelected = false
                                    singleCell.isUnavailable = true
                                    singleCell.isHighlighted = false
                                    selectedCells.add(singleCell)
                                } else if (!deactivatedDates.contains(singleCell.date.day + 1)) {
                                    singleCell.isSelected = true
                                    singleCell.isDeactivated = false
                                    singleCell.rangeState = RangeState.MIDDLE
                                    selectedCells.add(singleCell)
                                } else {
                                    singleCell.isSelected = true
                                    singleCell.isDeactivated = true
                                    singleCell.rangeState = RangeState.MIDDLE
                                    selectedCells.add(singleCell)
                                }

                            }
                        }
                    }
                }
            }
        }

        // Update the adapter.
        validateAndUpdate()
        return date != null
    }

    private fun monthKey(cal: Calendar): String {
        return cal.get(YEAR).toString() + "-" + cal.get(MONTH)
    }

    private fun monthKey(month: MonthDescriptor): String {
        return month.year.toString() + "-" + month.month
    }

    private fun clearOldSelections() {
        for (selectedCell in selectedCells) {
            // De-select the currently-selected cell.
            selectedCell.isSelected = false
            if (highlightedCells.contains(selectedCell)) {
                selectedCell.isUnavailable = false
                selectedCell.isHighlighted = true
            }

            if (dateListener != null) {
                val selectedDate = selectedCell.date

                if (selectionMode == SelectionMode.RANGE) {
                    val index = selectedCells.indexOf(selectedCell)
                    if (index == 0 || index == selectedCells.size - 1) {
                        dateListener!!.onDateUnselected(selectedDate)
                    }
                } else {
                    dateListener!!.onDateUnselected(selectedDate)
                }
            }
        }
        selectedCells.clear()
        selectedCals.clear()
    }

    private fun applyMultiSelect(date: Date?, selectedCal: Calendar): Date? {
        var date = date
        for (selectedCell in selectedCells) {
            if (selectedCell.date == date) {
                // De-select the currently-selected cell.
                selectedCell.isSelected = false
                selectedCells.remove(selectedCell)
                date = null
                break
            }
        }
        for (cal in selectedCals) {
            if (sameDate(cal, selectedCal)) {
                selectedCals.remove(cal)
                break
            }
        }
        return date
    }

    fun highlightDates(dates: Collection<Date>) {
        for (date in dates) {
            validateDate(date)

            val monthCellWithMonthIndex = getMonthCellWithIndexByDate(date)
            if (monthCellWithMonthIndex != null) {
                val newlyHighlightedCal = Calendar.getInstance(timeZone, locale)
                newlyHighlightedCal.time = date
                val cell = monthCellWithMonthIndex.cell

                highlightedCells.add(cell)
                highlightedCals.add(newlyHighlightedCal)
                cell.isHighlighted = true
            }
        }

        validateAndUpdate()
    }

    fun deactivateDates(deactivatedDates: ArrayList<Int>) {
        this.deactivatedDates = deactivatedDates
        validateAndUpdate()
    }

    fun clearSelectedDates() {
        for (selectedCell in selectedCells) {
            selectedCell.rangeState = RangeState.NONE
        }

        clearOldSelections()
        validateAndUpdate()
    }

    fun clearHighlightedDates() {
        for (cal in highlightedCells) {
            cal.isHighlighted = false
        }
        highlightedCells.clear()
        highlightedCals.clear()

        validateAndUpdate()
    }

    /**
     * Hold a cell with a month-index.
     */
    private class MonthCellWithMonthIndex(var cell: MonthCellDescriptor, var monthIndex: Int)

    /**
     * Return cell and month-index (for scrolling) for a given Date.
     */
    private fun getMonthCellWithIndexByDate(date: Date): MonthCellWithMonthIndex? {
        val searchCal = Calendar.getInstance(timeZone, locale)
        searchCal.time = date
        val monthKey = monthKey(searchCal)
        val actCal = Calendar.getInstance(timeZone, locale)

        val index = cells.getIndexOfKey(monthKey)
        val monthCells = cells[monthKey]
        for (weekCells in monthCells!!) {
            for (actCell in weekCells) {
                actCal.time = actCell.date
                if (sameDate(actCal, searchCal) && actCell.isSelectable) {
                    return MonthCellWithMonthIndex(actCell, index)
                }
            }
        }
        return null
    }

    private inner class MonthAdapter() : RecyclerView.Adapter<MonthAdapter.MyHolder>() {
        private val inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(context)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view = MonthView.create(parent, inflater, weekdayNameFormat!!, listener, today, dividerColor,
                    dayBackgroundResId, dayTextColorResId, titleTextColor, displayHeader,
                    headerTextColor, this@CalendarPickerView.decorators, locale!!, dayViewAdapter)

            view.setTag(R.id.day_view_adapter_class, dayViewAdapter.javaClass)

            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            var position = position
            val view = holder.itemView as MonthView
            view.decorators = this@CalendarPickerView.decorators
            if (monthsReverseOrder) {
                position = months.size - position - 1
            }
            cells.getValueAtIndex(position)?.let {
                view.init(months[position], it, displayOnly,
                    titleTypeface, dateTypeface, deactivatedDates, subTitles)
            }

        }


        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemCount(): Int {
            return months.size
        }

        inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    internal fun getMonthCells(month: MonthDescriptor, startCal: Calendar): List<List<MonthCellDescriptor>> {
        val cal = Calendar.getInstance(timeZone, locale)
        cal.time = startCal.time
        val cells = ArrayList<List<MonthCellDescriptor>>()
        cal.set(DAY_OF_MONTH, 1)
        val firstDayOfWeek = cal.get(DAY_OF_WEEK)
        var offset = cal.firstDayOfWeek - firstDayOfWeek
        if (offset > 0) {
            offset -= 7
        }
        cal.add(Calendar.DATE, offset)

        val minSelectedCal = minDate(selectedCals)
        val maxSelectedCal = maxDate(selectedCals)

        while ((cal.get(MONTH) < month.month + 1 || cal.get(YEAR) < month.year) //
                && cal.get(YEAR) <= month.year) {
            Logr.d("Building week row starting at %s", cal.time)
            val weekCells = ArrayList<MonthCellDescriptor>()
            cells.add(weekCells)
            for (c in 0..6) {
                val date = cal.time
                val isCurrentMonth = cal.get(MONTH) == month.month
                val isSelected = isCurrentMonth && containsDate(selectedCals, cal)
                val isSelectable = isCurrentMonth && betweenDates(cal, minCal, maxCal) && isDateSelectable(date)
                val isToday = sameDate(cal, today)
                val isHighlighted = containsDate(highlightedCals, cal)
                val value = cal.get(DAY_OF_MONTH)

                var rangeState = RangeState.NONE
                if (selectedCals.size > 1) {
                    if (sameDate(minSelectedCal!!, cal)) {
                        rangeState = RangeState.FIRST
                    } else if (sameDate(maxDate(selectedCals)!!, cal)) {
                        rangeState = RangeState.LAST
                    } else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
                        rangeState = RangeState.MIDDLE
                    }
                }

                weekCells.add(
                        MonthCellDescriptor(date, isCurrentMonth, isSelectable, isSelected, isToday,
                                isHighlighted, value, rangeState))
                cal.add(DATE, 1)
            }
        }
        return cells
    }

    private fun containsDate(selectedCals: List<Calendar>, date: Date): Boolean {
        val cal = Calendar.getInstance(timeZone, locale)
        cal.time = date
        return containsDate(selectedCals, cal)
    }

    private fun isDateSelectable(date: Date): Boolean {
        return dateConfiguredListener == null || dateConfiguredListener!!.isDateSelectable(date)
    }

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        dateListener = listener
    }

    /**
     * Set a listener to react to user selection of a disabled date.
     *
     * @param listener the listener to set, or null for no reaction
     */
    fun setOnInvalidDateSelectedListener(listener: OnInvalidDateSelectedListener) {
        invalidDateListener = listener
    }

    /**
     * Set a listener used to discriminate between selectable and unselectable dates. Set this to
     * disable arbitrary dates as they are rendered.
     *
     *
     * Important: set this before you call [.init] methods.  If called afterwards,
     * it will not be consistently applied.
     */
    fun setDateSelectableFilter(listener: DateSelectableFilter) {
        dateConfiguredListener = listener
    }

    /**
     * Set an adapter used to initialize [CalendarCellView] with custom layout.
     *
     *
     * Important: set this before you call [.init] methods.  If called afterwards,
     * it will not be consistently applied.
     */
    fun setCustomDayView(dayViewAdapter: DayViewAdapter) {
        this.dayViewAdapter = dayViewAdapter
        adapter?.notifyDataSetChanged()
    }

    /**
     * Set a listener to intercept clicks on calendar cells.
     */
    fun setCellClickInterceptor(listener: CellClickInterceptor) {
        cellClickInterceptor = listener
    }

    /**
     * Interface to be notified when a new date is selected or unselected. This will only be called
     * when the user initiates the date selection.  If you call [.selectDate] this
     * listener will not be notified.
     *
     * @see .setOnDateSelectedListener
     */
    interface OnDateSelectedListener {
        fun onDateSelected(date: Date)

        fun onDateUnselected(date: Date)
    }

    /**
     * Interface to be notified when an invalid date is selected by the user. This will only be
     * called when the user initiates the date selection. If you call [.selectDate] this
     * listener will not be notified.
     *
     * @see .setOnInvalidDateSelectedListener
     */
    interface OnInvalidDateSelectedListener {
        fun onInvalidDateSelected(date: Date)
    }

    /**
     * Interface used for determining the selectability of a date cell when it is configured for
     * display on the calendar.
     *
     * @see .setDateSelectableFilter
     */
    interface DateSelectableFilter {
        fun isDateSelectable(date: Date): Boolean
    }

    /**
     * Interface to be notified when a cell is clicked and possibly intercept the click.  Return true
     * to intercept the click and prevent any selections from changing.
     *
     * @see .setCellClickInterceptor
     */
    interface CellClickInterceptor {
        fun onCellClicked(date: Date): Boolean
    }

    private inner class DefaultOnInvalidDateSelectedListener : OnInvalidDateSelectedListener {
        override fun onInvalidDateSelected(date: Date) {}
    }

    companion object {

        /**
         * Returns a string summarizing what the client sent us for init() params.
         */
        private fun dbg(minDate: Date?, maxDate: Date?): String {
            return "minDate: $minDate\nmaxDate: $maxDate"
        }

        /**
         * Clears out the hours/minutes/seconds/millis of a Calendar.
         */
        internal fun setMidnight(cal: Calendar) {
            cal.set(HOUR_OF_DAY, 0)
            cal.set(MINUTE, 0)
            cal.set(SECOND, 0)
            cal.set(MILLISECOND, 0)
        }

        private val TAG = CalendarPickerView::class.java.simpleName

        private fun containsDate(selectedCals: List<Calendar>, cal: Calendar): Boolean {
            for (selectedCal in selectedCals) {
                if (sameDate(cal, selectedCal)) {
                    return true
                }
            }
            return false
        }

        private fun minDate(selectedCals: List<Calendar>?): Calendar? {
            if (selectedCals == null || selectedCals.size == 0) {
                return null
            }
            Collections.sort(selectedCals)
            return selectedCals[0]
        }

        private fun maxDate(selectedCals: List<Calendar>?): Calendar? {
            if (selectedCals == null || selectedCals.size == 0) {
                return null
            }
            Collections.sort(selectedCals)
            return selectedCals[selectedCals.size - 1]
        }

        private fun sameDate(cal: Calendar, selectedDate: Calendar): Boolean {
            return (cal.get(MONTH) == selectedDate.get(MONTH)
                    && cal.get(YEAR) == selectedDate.get(YEAR)
                    && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH))
        }

        private fun betweenDates(cal: Calendar, minCal: Calendar?, maxCal: Calendar?): Boolean {
            val date = cal.time
            return betweenDates(date, minCal!!, maxCal)
        }

        internal fun betweenDates(date: Date, minCal: Calendar, maxCal: Calendar?): Boolean {
            val min = minCal.time
            return ((date == min || date.after(min)) // >= minCal
                    && date.before(maxCal!!.time)) // && < maxCal
        }

        private fun sameMonth(cal: Calendar, month: MonthDescriptor): Boolean {
            return cal.get(MONTH) == month.month && cal.get(YEAR) == month.year
        }
    }
}
