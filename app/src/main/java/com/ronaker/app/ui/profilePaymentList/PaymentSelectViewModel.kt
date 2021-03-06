package com.ronaker.app.ui.profilePaymentList

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.model.PaymentCard

class PaymentSelectViewModel {
    val cardTypeImage = MutableLiveData<Int>()
    val title = MutableLiveData<String?>()

    val selected = MutableLiveData<Boolean>()

    fun bind(
        data: PaymentCard
    ) {


        when (data.paymentInfoType?.let { PaymentCard.PaymentType[it] }) {


            PaymentCard.PaymentType.Cash -> {
                cardTypeImage.value = R.drawable.ic_card_empty
                title.value = "By Cash"
            }
            PaymentCard.PaymentType.CreditCard -> {

                data.cardNumber?.let {
                    PaymentCard.CardType.detectFast(it).apply {
                        cardTypeImage.value = this.resource
                    }
                }


                val name = StringBuilder()
                data.cardNumber?.let {


                    name.append(it.substring(0, 4))

                    name.append("*".repeat(it.length - 8))

                    name.append(it.substring(it.length - 5, it.length - 1))


                }

                title.value = name.toString()
            }
            PaymentCard.PaymentType.PayPal -> {
                title.postValue( data.cardNumber?:"")
            }
            else -> {

            }

        }



        selected.value = data.selected


    }


}