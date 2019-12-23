package com.ronaker.app.ui.profilePaymentHistoryList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.PaymentCard

class PaymentHistoryViewModel(val app: Application) : BaseViewModel(app) {
    val cardTypeImage = MutableLiveData<Int>()
    val title = MutableLiveData<String>()


    fun bind(
        data: PaymentCard
    ) {

        cardTypeImage.value=PaymentCard.CardType[data.type].resource

        title.value=data.title





    }


}