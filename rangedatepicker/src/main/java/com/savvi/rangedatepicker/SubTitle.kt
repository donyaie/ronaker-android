package com.savvi.rangedatepicker

import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class SubTitle(val date: Date, val title: String) {
    companion object {

        fun getByDate(subTitles: ArrayList<SubTitle>?, date: Date): SubTitle? {
            if (subTitles != null && subTitles.size > 0) {
                for (subTitle in subTitles) {
                    if (isSameDay(subTitle.date, date)) {
                        return subTitle
                    }
                }
            }
            return null
        }

        private fun isSameDay(date1: Date, date2: Date): Boolean {
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal1.time = date1
            cal2.time = date2
            return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
        }
    }
}
