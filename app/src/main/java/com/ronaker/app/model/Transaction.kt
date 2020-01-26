package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.FinancialTransactionsResponseModel
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Transaction(
    var suid: String? = null,
    val amount: Double? = null,
    val transactionType: String? = null,
    val transactionStatus: String? = null,
    val description: String? = null,
    val createAt: Date?=null,
    val product_suid:String?=null
) : Parcelable {

    enum class TransactionTypeEnum constructor(key: String) {

        Debit("debit"),
        Credit("credit"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String?): TransactionTypeEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position?.compareTo(stateEnum.key,true) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }

    enum class TransactionStatusEnum constructor(key: String) {
        Successful("successful"),
        Failed("failed"),
        OnHold("on_hold"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String?): TransactionStatusEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position?.compareTo(stateEnum.key,true)  == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }



}


fun List<FinancialTransactionsResponseModel>.mapToTransactionList(): List<Transaction> {

    val list: ArrayList<Transaction> = ArrayList()

    this.forEach {

        val value = Transaction(
            it.suid,
            it.amount,
            it.transaction_type,
            it.transaction_status,
            it.description,
            it.created_at,
            it.product_suid
        )

        list.add(value)
    }

    return list

}
