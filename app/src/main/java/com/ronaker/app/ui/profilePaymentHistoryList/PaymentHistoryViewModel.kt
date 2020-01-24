package com.ronaker.app.ui.profilePaymentHistoryList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Transaction
import com.ronaker.app.utils.toCurrencyFormat

class PaymentHistoryViewModel(val app: Application) : BaseViewModel(app) {
    val amount = MutableLiveData<String>()
    val transactionType = MutableLiveData<Int>()
    val transactionStatus = MutableLiveData<Int>()
    val description = MutableLiveData<String>()


    fun bind(
        data: Transaction
    ) {

        description.value=data.description


        when(Transaction.TransactionStatusEnum[data.transactionStatus]){

            Transaction.TransactionStatusEnum.Successful-> transactionStatus.value=R.drawable.ic_guide_success
            Transaction.TransactionStatusEnum.Failed-> transactionStatus.value=R.drawable.ic_guide_red
            Transaction.TransactionStatusEnum.OnHold-> transactionStatus.value=R.drawable.ic_guide_dark
            else-> transactionStatus.value=R.drawable.ic_guide_success
        }


        when(Transaction.TransactionTypeEnum[data.transactionType]){

            Transaction.TransactionTypeEnum.Debit->
            {
                transactionType.value= R.color.colorRed

                amount.value=((data.amount?:0.0)/100.0).toCurrencyFormat("-")
            }
            Transaction.TransactionTypeEnum.Credit->{
                transactionType.value= R.color.colorGreen

                amount.value=((data.amount?:0.0)/100.0).toCurrencyFormat("+")
            }
            else->{
                transactionType.value= R.color.colorTextDark

                amount.value=((data.amount?:0.0)/100.0).toCurrencyFormat()
            }
        }


    }


}