package com.ronaker.app.model

import com.ronaker.app.data.network.response.OrderResponseModel
import java.util.*


data class Order(val suid: String,
                 val message: String,
                 val fromDate: Date,
                 val toDate: Date,
                 val price: Double,
                 val orderType:String,
                 val status:String,
                 val product:Product){

}



fun List<OrderResponseModel>.toOrderList(): List<Order> {


    var list: ArrayList<Order> = ArrayList()

    this.forEach {

        var product = Order(
            it.suid,
            it.message,
            it.start_date,
            it.end_date,
            it.price,
            it.order_type,
            it.status,
            it.product.toProduct()
        )

        list.add(product)
    }

    return list

}






