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

        data.cardNumber?.let {

            PaymentCard.CardType.detectFast(it) .apply {



                cardTypeImage.value=this.resource


            }



        }



        title.value=data.cardNumber
    }


}