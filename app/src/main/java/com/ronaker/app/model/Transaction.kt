package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.R
import com.ronaker.app.data.network.request.PaymentInfoCreateRequestModel
import com.ronaker.app.data.network.response.FinancialTransactionsResponseModel
import com.ronaker.app.data.network.response.PaymentInfoListResponseModel
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.regex.Pattern


@Parcelize
data class Transaction(
    var suid: String? = null,
    val amount: Double? = null,
    val transactionType: String? = null,
    val transactionStatus: String? = null,
    val description: String? = null
) : Parcelable {



}


fun List<FinancialTransactionsResponseModel>.mapToTransactionList(): List<Transaction> {

    val list: ArrayList<Transaction> = ArrayList()

    this.forEach {

        val value = Transaction(
            it.suid,
            it.amount,
            it.transaction_type,
            it.transaction_status,
            it.description
        )

        list.add(value)
    }

    return list

}
