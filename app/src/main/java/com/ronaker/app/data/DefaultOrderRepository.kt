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

class DefaultOrderRepository(private val api: OrderApi,
                             private val userRepository: UserRepository) :
    OrderRepository {

    override fun getOrders(
        page: Int,
        filter: String?
    ): Observable<Result<ListResponseModel<Order>>> {

        return api.getOrders(userRepository.getUserAuthorization(), page, filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toOrderList())
            }
            .toResult()

    }

    override fun getOrderDetail( suid: String): Observable<Result<Order>> {

        return api.getOrderDetail(userRepository.getUserAuthorization(), suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {
                it.toOrderModel()
            }
            .toResult()

    }


    override fun createOrder(
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

        return api.createOrder(userRepository.getUserAuthorization(), request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                true
            }
            .toResult()

    }


    override fun updateOrderStatus(
        suid: String,
        status: String?,
        address: String?,
        instruction: String?,
        reason: String?,
        isArchived: Boolean?
    ): Observable<Result<Boolean>> {

        val request =
            OrderUpdateRequestModel(status, address, instruction, reason, isArchived)

        return api.updateOrderStatus(userRepository.getUserAuthorization(), suid, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { true }.toResult()

    }

    override fun orderRate(
        orderSuid: String,
        comment: String,
        stars: Double
    ): Observable<Result<Boolean>> {

        val request =
            ProductRateRequestModel(stars, comment)

        return api.orderRate(userRepository.getUserAuthorization(), orderSuid, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { true }.toResult()

    }


    override fun getSmartIDVerificationCode(
        orderSuid: String
    ): Observable<Result<String>> {

        return api.getSmartIDVerificationCode(userRepository.getUserAuthorization(), orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.verification_code
            }
            .toResult()

    }


    override fun startSmartIDAuth(
        orderSuid: String
    ): Observable<Result<Boolean>> {

        return api.startSmartIDAuth(userRepository.getUserAuthorization(), orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


    override fun checkSmartIDSession(
        orderSuid: String

    ): Observable<Result<Boolean>> {
        return api.checkSmartIDSession(userRepository.getUserAuthorization(), orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }



    override fun startSmartIDCert(

        orderSuid: String
    ): Observable<Result<Boolean>> {

        return api.startSmartIDCert(userRepository.getUserAuthorization(), orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


    override fun checkSmartIDSessionCert(
        orderSuid: String
    ): Observable<Result<Boolean>> {
        return api.checkSmartIDSessionCert(userRepository.getUserAuthorization(), orderSuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


}