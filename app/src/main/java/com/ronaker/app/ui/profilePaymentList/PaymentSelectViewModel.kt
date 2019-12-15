package com.ronaker.app.ui.profilePaymentList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Order
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.utils.toCurrencyFormat

class PaymentSelectViewModel(val app: Application) : BaseViewModel(app) {
    val cardTypeImage = MutableLiveData<Int>()
    val title = MutableLiveData<String>()

    val selected= MutableLiveData<Boolean>()

    fun bind(
        data: PaymentCard
    ) {

        cardTypeImage.value=PaymentCard.CardType[data.type].resource

        title.value=data.title

        selected.value=data.selected




    }


}