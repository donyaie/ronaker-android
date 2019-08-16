package com.ronaker.app.data


import com.ronaker.app.base.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.CategoryApi
import com.ronaker.app.data.network.OrderApi
import com.ronaker.app.data.network.request.OrderCreateRequestModel
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class OrderRepository(private val api: OrderApi, private val preferencesProvider: PreferencesProvider) {



    fun getOrders(token: String?,filter:String?): Observable<Result<ListResponseModel<OrderResponseModel>>> {

        return api.getOrders("Token $token",filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }



    fun createOrder(token: String?, product_suid:String, stateDate: Date,endDate:Date,message:String? ): Observable<Result<FreeResponseModel>> {

        var request=OrderCreateRequestModel(product_suid,stateDate,endDate,message)

        return api.createOrder("Token $token",request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }





}

