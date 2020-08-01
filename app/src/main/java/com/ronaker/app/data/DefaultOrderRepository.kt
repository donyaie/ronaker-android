package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.OrderApi
import com.ronaker.app.data.network.request.OrderCreateRequestModel
import com.ronaker.app.data.network.request.OrderUpdateRequestModel
import com.ronaker.app.data.network.request.ProductRateRequestModel
import com.ronaker.app.data.network.request.UserSmartIdVerificationCodeRequestModel
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.Order
import com.ronaker.app.model.toOrderList
import com.ronaker.app.model.toOrderModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class DefaultOrderRepository(private val api: OrderApi) :
    OrderRepository {

    override fun getOrders(
        token: String?,
        page: Int,
        filter: String?
    ): Observable<Result<ListResponseModel<Order>>> {

        return api.getOrders("Token $token", page, filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toOrderList())
            }
            .toResult()

    }

    override fun getOrderDetail(token: String?, suid: String): Observable<Result<Order>> {

        return api.getOrderDetail("Token $token", suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {
                it.toOrderModel()
            }
            .toResult()

    }


    override fun createOrder(
        token: String?,
        product_suid: String,
        stateDate: Date,
        endDate: Date,
        message: String?,
        price: Double
    ): Observable<Result<Boolean>> {

        val request =
            OrderCreateRequestModel(
                product_suid,
                stateDate,
                endDate,
                message,
                price
            )

        return api.createOrder("Token $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                true
            }
            .toResult()

    }


    override fun updateOrderStatus(
        token: String?,
        suid: String,
        status: String?,
        address: String?,
        instruction: String?,
        reason: String?,
        isArchived: Boolean?
    ): Observable<Result<Boolean>> {

        val request =
            OrderUpdateRequestModel(status, address, instruction, reason, isArchived)

        return api.updateOrderStatus("Token $token", suid, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { true }.toResult()

    }

    override fun orderRate(
        token: String?,
        orderSuid: String,
        comment: String,
        stars: Double
    ): Observable<Result<Boolean>> {

        val request =
            ProductRateRequestModel(stars, comment)

        return api.orderRate("Token $token", orderSuid, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { true }.toResult()

    }


    override fun getSmartIDVerificationCode(

        user_token: String?,
        orderSuid: String
    ): Observable<Result<String>> {

        return api.getSmartIDVerificationCode("Token $user_token", orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.verification_code
            }
            .toResult()

    }


    override fun startSmartIDAuth(

        user_token: String?,
        orderSuid: String,
        national_code: String,
        personal_code: String
    ): Observable<Result<Boolean>> {

        val request =
            UserSmartIdVerificationCodeRequestModel(
                national_code,
                personal_code
            )
        return api.startSmartIDAuth("Token $user_token", orderSuid, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


    override fun checkSmartIDSession(
        user_token: String?,
        orderSuid: String

    ): Observable<Result<Boolean>> {
        return api.checkSmartIDSession("Token $user_token", orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


}