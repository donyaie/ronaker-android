package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.model.Transaction
import io.reactivex.Observable


interface PaymentInfoRepository {
    fun getPaymentInfoList(): Observable<Result<List<PaymentCard>?>>
    fun addPaymentInfo( payment: PaymentCard): Observable<Result<Boolean>>
    fun getFinancialTransactions(): Observable<Result<List<Transaction>?>>
}