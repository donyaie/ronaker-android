package com.ronaker.app.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat


fun Double.toCurrencyFormat(): String {

    val format: NumberFormat = NumberFormat.getCurrencyInstance()

//    format.currency = Currency.getInstance("EUR")


    val decimalFormatSymbols: DecimalFormatSymbols =
        (format as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol ="€"



    format.decimalFormatSymbols=decimalFormatSymbols

    return format.format(this)
}