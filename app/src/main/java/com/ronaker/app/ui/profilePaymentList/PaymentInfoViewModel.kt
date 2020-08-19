package com.ronaker.app.ui.profilePaymentList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.PaymentCard

class PaymentInfoViewModel {
    val cardTypeImage = MutableLiveData<Int>()
    val title = MutableLiveData<String>()


    val verifyImage = MutableLiveData<Int>()

    fun bind(
        data: PaymentCard
    ) {
        data.cardNumber?.let {
            PaymentCard.CardType.detectFast(it).apply { cardTypeImage.value = this.resource }
        }

        if (data.isVerified == true) {
            verifyImage.value = R.drawable.ic_guide_success
        } else {
            verifyImage.value = R.drawable.ic_field_empty
        }


        val name = StringBuilder()
        data.cardNumber?.let {


            name.append(it.substring(0, 4))

            name.append("*".repeat(it.length - 8))

            name.append(it.substring(it.length - 5, it.length - 1))


        }
        title.value = name.toString()


    }


}