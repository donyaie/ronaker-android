package com.savvi.rangedatepicker

import java.util.Date

class MonthDescriptor(val month: Int, val year: Int, val date: Date, var label: String?) {

    override fun toString(): String {
        return ("MonthDescriptor{"
                + "label='"
                + label
                + '\''.toString()
                + ", month="
                + month
                + ", year="
                + year
                + '}'.toString())
    }
}
