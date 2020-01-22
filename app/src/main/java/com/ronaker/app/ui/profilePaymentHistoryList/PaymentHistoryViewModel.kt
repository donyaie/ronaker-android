package com.ronaker.app.ui.profilePaymentHistoryList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Transaction
import com.ronaker.app.utils.toCurrencyFormat

class PaymentHistoryViewModel(val app: Application) : BaseViewModel(app) {
    val amount = MutableLiveData<String>()
    val transactionType = MutableLiveData<String>()
    val transactionStatus = MutableLiveData<String>()
    val description = MutableLiveData<String>()


    fun bind(
        data: Transaction
    ) {



        amount.value=data.amount?.toCurrencyFormat()
        description.value=data.description
        transactionType.value=data.transactionType
        transactionStatus.value=data.transactionStatus


    }


}