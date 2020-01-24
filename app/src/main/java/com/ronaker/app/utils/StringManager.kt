package com.ronaker.app.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat


fun Double.toCurrencyFormat(): String {

    return this.toCurrencyFormat(null)
}


fun Double.toCurrencyFormat(prefix:String?): String {

    val format: NumberFormat = NumberFormat.getCurrencyInstance()

//    format.currency = Currency.getInstance("EUR")


    val decimalFormatSymbols: DecimalFormatSymbols =
        (format as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol ="€" + (if(prefix==null) "" else " $prefix")

    format.decimalFormatSymbols=decimalFormatSymbols

    if((this*100.0)%100.0==0.0){
        format.maximumFractionDigits=0
    }else
        format.minimumFractionDigits=2

    return format.format(this)
}