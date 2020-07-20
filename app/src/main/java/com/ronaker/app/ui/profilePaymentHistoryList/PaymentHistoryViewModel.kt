package com.ronaker.app.ui.profilePaymentHistoryList

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.model.Transaction
import com.ronaker.app.utils.toCurrencyFormat
import java.text.SimpleDateFormat
import java.util.*

class PaymentHistoryViewModel(val app: Application) : BaseViewModel(app) {
    val amount = MutableLiveData<String>()
    val transactionType = MutableLiveData<Int>()
    val transactionStatus = MutableLiveData<Int>()
    val description = MutableLiveData<String>()

    val cardNumber = MutableLiveData<String>()

    val cardImage = MutableLiveData<Int>()
    val cardVisibility = MutableLiveData<Int>()

    val descriptionVisibility = MutableLiveData<Int>()
    val createMonth = MutableLiveData<String>()
    val createDay = MutableLiveData<String>()

    val orderVisibility = MutableLiveData<Int>()


    fun bind(
        data: Transaction
    ) {

        description.value = data.description

        if (data.description.isNullOrBlank()) {
            descriptionVisibility.value = View.GONE
        } else
            descriptionVisibility.value = View.VISIBLE


        when (Transaction.TransactionStatusEnum[data.transactionStatus]) {

            Transaction.TransactionStatusEnum.Successful -> transactionStatus.value =
                R.color.colorGreen
            Transaction.TransactionStatusEnum.Failed -> transactionStatus.value = R.color.colorRed
            Transaction.TransactionStatusEnum.OnHold -> transactionStatus.value =
                R.color.colorTextDark
            else -> transactionStatus.value = R.color.colorTextDark
        }


        when (Transaction.TransactionTypeEnum[data.transactionType]) {

            Transaction.TransactionTypeEnum.Debit -> {
                transactionType.value = R.color.colorRed

                amount.value = ((data.amount ?: 0.0) / 100.0).toCurrencyFormat("-")
            }
            Transaction.TransactionTypeEnum.Credit -> {
                transactionType.value = R.color.colorGreen

                amount.value = ((data.amount ?: 0.0) / 100.0).toCurrencyFormat("+")
            }
            else -> {
                transactionType.value = R.color.colorTextDark

                amount.value = ((data.amount ?: 0.0) / 100.0).toCurrencyFormat()
            }
        }



        data.createAt?.let {


            createMonth.value = SimpleDateFormat("MMMM", Locale.getDefault()).format(it)
            createDay.value = SimpleDateFormat("dd", Locale.getDefault()).format(it)

        }



        data.paymentInfo?.card_number?.let {
            PaymentCard.CardType.detectFast(it).apply { cardImage.value = this.resource }

            val name = StringBuilder()

            name.append(it.substring(0, 4))

            name.append("*".repeat(it.length - 8))

            name.append(it.substring(it.length - 5, it.length - 1))

            cardNumber.value = name.toString()
            cardVisibility.value = View.VISIBLE

        } ?: run {
            cardVisibility.value = View.GONE
        }



        orderVisibility.value = if (data.OrderSuid == null) View.GONE else View.VISIBLE


    }


}