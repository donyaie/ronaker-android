package com.ronaker.app.data


import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.CategoryApi
import com.ronaker.app.data.network.PaymentInfoApi
import com.ronaker.app.data.network.response.CategoriesResponseModel
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DefaultPaymentInfoRepository(private val api: PaymentInfoApi) : PaymentInfoRepository {


     override fun getPaymentInfoList(token: String?): Observable<Result<List<PaymentCard>?>> {

        return api.getPaymentInfoList("Token $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.results?.toPaymentInfoList()
            }
            .toResult()
    }


    override fun addPaymentInfo(token: String?, payment:PaymentCard): Observable<Result<Boolean>> {

        return api.addPaymentInfo("Token $token",payment.toPaymentCardCreateModel())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
               true
            }
            .toResult()
    }


}

