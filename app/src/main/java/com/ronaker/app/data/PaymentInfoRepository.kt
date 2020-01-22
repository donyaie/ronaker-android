package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.model.PaymentCard
import io.reactivex.Observable


interface PaymentInfoRepository {
    fun getPaymentInfoList(token: String?): Observable<Result<List<PaymentCard>?>>
    fun addPaymentInfo(token: String?, payment: PaymentCard): Observable<Result<Boolean>>
}