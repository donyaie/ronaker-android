package com.savvi.rangedatepicker

import java.util.Date

interface CalendarCellDecorator {
    fun decorate(cellView: CalendarCellView, date: Date)
}
