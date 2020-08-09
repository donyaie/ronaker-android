package com.ronaker.app.utils



fun String.isNumeric():Boolean{

    val isValid=true
    this.forEach {
        if(!it.isDigit())
            return false

    }
    return isValid
}

