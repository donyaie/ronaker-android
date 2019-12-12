package com.ronaker.app.utils

import java.text.NumberFormat
import java.util.*


fun Double.toCurrencyFormat(): String {

    val format: NumberFormat = NumberFormat.getCurrencyInstance()

    if (((this * 100.0) % 100) > 0)
        format.maximumFractionDigits = 2
    else
        format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("EUR")
    return format.format(this)
}