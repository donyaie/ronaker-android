package com.ronaker.app.data


import com.ronaker.app.data.local.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.local.PreferencesDataSource
import com.ronaker.app.data.network.OrderApi
import com.ronaker.app.data.network.request.OrderCreateRequestModel
import com.ronaker.app.data.network.request.OrderUpdateRequestModel
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class OrderRepository(private val api: OrderApi) {



    fun getOrders(token: String?,filter:String?): Observable<Result<ListResponseModel<OrderResponseModel>>> {

        return api.getOrders("Token $token",filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }



    fun createOrder(token: String?, product_suid:String, stateDate: Date,endDate:Date,message:String? ,price:Double ): Observable<Result<FreeResponseModel>> {

        var request=OrderCreateRequestModel(product_suid,stateDate,endDate,message,price)

        return api.createOrder("Token $token",request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun updateOrderStatus(token: String?,suid:String, status:String ): Observable<Result<FreeResponseModel>> {

        var request=OrderUpdateRequestModel(status)

        return api.updateOrderStatus("Token $token",suid,request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }



}

