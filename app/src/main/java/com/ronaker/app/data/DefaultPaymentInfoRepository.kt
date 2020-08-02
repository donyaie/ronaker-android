package com.ronaker.app.data


import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.PaymentInfoApi
import com.ronaker.app.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DefaultPaymentInfoRepository(
    private val api: PaymentInfoApi,
    private val userRepository: UserRepository
) : PaymentInfoRepository {


    override fun getPaymentInfoList(): Observable<Result<List<PaymentCard>?>> {

        return api.getPaymentInfoList(userRepository.getUserAuthorization())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.results?.toPaymentInfoList()
            }
            .toResult()
    }


    override fun addPaymentInfo(payment: PaymentCard): Observable<Result<Boolean>> {

        return api.addPaymentInfo(userRepository.getUserAuthorization(), payment.toPaymentCardCreateModel())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()
    }


    override fun getFinancialTransactions(): Observable<Result<List<Transaction>?>> {

        return api.getFinancialTransactions(userRepository.getUserAuthorization())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                it.results?.mapToTransactionList()
            }
            .toResult()
    }


}

