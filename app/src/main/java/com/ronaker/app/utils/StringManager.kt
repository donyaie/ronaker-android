package com.ronaker.app.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


fun Double.toCurrencyFormat(): String {

    return this.toCurrencyFormat(null)
}


fun nameFormat(firstName: String?, lastName: String?): String {
    return "${firstName?.toLowerCase(Locale.ROOT)?.capitalize() ?: ""} ${
        lastName?.toLowerCase(
            Locale.ROOT
        )?.capitalize() ?: ""
    }"


}


fun String.capitalize(): String {


    return this.split(" ").filter { it.isNotEmpty() }.joinToString(separator = " ") {
        if (it.contains("-")) {
            it.split("-").filter { name -> name.isNotEmpty() }
                .joinToString(separator = "-") { name ->
                    "${name[0].toUpperCase()}${
                        name.substring(1).toLowerCase(Locale.getDefault())
                    }"
                }


        } else {
            "${it[0].toUpperCase()}${
                it.substring(1).toLowerCase(Locale.getDefault())
            }"
        }


    }

}


fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

fun Double.toCurrencyFormat(prefix: String?, includeSymbol: Boolean = true): String {

    val format: NumberFormat = NumberFormat.getCurrencyInstance()

//    format.currency = Currency.getInstance("EUR")


    val decimalFormatSymbols: DecimalFormatSymbols =
        (format as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol = ""

    format.decimalFormatSymbols = decimalFormatSymbols

    if ((this * 100.0) % 100.0 == 0.0) {
        format.maximumFractionDigits = 0
    } else
        format.minimumFractionDigits = 2




    return "${if (prefix == null) "" else "$prefix "}${if (includeSymbol) "€" else ""}${format.format(this)
    }"
}