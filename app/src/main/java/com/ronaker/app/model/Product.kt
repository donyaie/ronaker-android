package com.ronaker.app.model

import com.ronaker.app.data.network.response.ProductItemResponceModel

/**
 * Class which provides a model for User
 * @constructor Sets all properties of the post
 * @property suid the unique identifier of the user
 * @property email the unique email Address
 * @property is_email_verified if email verified is true
 * @property first_name the first name of user
 * @property phone_number the number of user
 * @property is_phone_number_verified if number verified is true
 */
data class Product(
    var suid: String?
    , var name: String?
    , var price_per_day: Double?
    , var price_per_week: Double?
    , var price_per_month: Double?
    , var description: String?
    , var avatar: String?
    , var images: ArrayList<ProductImage>?

) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )



    data class ProductImage(var url:String? ,
                            var suid:String?){

    }

}


fun List<ProductItemResponceModel>.toModel():List<Product> {


    var list:ArrayList<Product> = ArrayList()

    this.forEach {

        var product=Product(it.suid,it.name,it.price_per_day,it.price_per_week,it.price_per_month,it.description,it.avatar,null)

        list.add(product)
    }

    return list

}