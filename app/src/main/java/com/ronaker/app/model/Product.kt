package com.ronaker.app.model

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.response.LocationResponseModel
import com.ronaker.app.data.network.response.ProductDetailResponceModel
import com.ronaker.app.data.network.response.ProductItemImageResponceModel
import com.ronaker.app.data.network.response.ProductItemResponceModel
import kotlinx.android.parcel.Parcelize

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
@Parcelize
data class Product(
    var suid: String?
    , var name: String?
    , var price_per_day: Double?
    , var price_per_week: Double?
    , var price_per_month: Double?
    , var description: String?
    , var avatar: String?
    , var images: ArrayList<ProductImage>?
    , var categories: ArrayList<Category>?
    , var location: LatLng?
    , var address: String?
    , var avatar_suid: String? = null
    , var new_categories: ArrayList<String>? = null

) : Parcelable {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    @Parcelize
    data class ProductImage(
        var url: String?,
        var suid: String?, var uri: Uri? = null, var isLocal: Boolean = false
    ) : Parcelable {

        constructor() : this(null, null)


        val progress: MutableLiveData<Boolean> = MutableLiveData()

    }

}


fun List<ProductItemResponceModel>.toProductList(): List<Product> {


    var list: ArrayList<Product> = ArrayList()

    this.forEach {

        var product = it.toProduct()

        list.add(product)
    }

    return list

}


fun ProductItemResponceModel.toProduct(): Product {


    var product = Product(
        this.suid,
        this.name,
        this.price_per_day,
        this.price_per_week,
        this.price_per_month,
        this.description,
        this.avatar,
        if (this.images != null) this.images.toProductImage() as ArrayList<Product.ProductImage> else ArrayList(),
        if (this.categories != null) this.categories.toCategoryList() else ArrayList(),
        if (this.location != null) LatLng(location.lat, location.lng) else null,
        this.address
    )



    return product

}

fun ProductDetailResponceModel.toProductDetail(): Product {


    var product = Product(
        this.suid,
        this.name,
        this.price_per_day,
        this.price_per_week,
        this.price_per_month,
        this.description,
        this.avatar,
        if (this.images != null) this.images.toProductImage() as ArrayList<Product.ProductImage> else ArrayList(),
        if (this.categories != null) this.categories.toCategoryList() else ArrayList(),
        if (this.location != null) LatLng(location.lat, location.lng) else null,
        this.address
    )



    return product

}


fun List<ProductItemImageResponceModel>.toProductImage(): List<Product.ProductImage> {


    var list: ArrayList<Product.ProductImage> = ArrayList()

    this.forEach {

        var product = Product.ProductImage(it.url, it.suid)
        product.isLocal = false

        list.add(product)
    }

    return list

}


fun Product.toProductCreateModel(): ProductCreateRequestModel {


    var imageList: ArrayList<String>? = ArrayList<String>()


    if (this.images != null) {
        this.images?.forEach { it.suid?.let {suid->   imageList?.add(suid)} }

    }

    if (imageList?.size == 0) {
        imageList = null
    }


    var item = ProductCreateRequestModel(
        this.name,
        this.price_per_day,
        this.price_per_week,
        this.price_per_month,
        this.description,
        this.avatar_suid,
        imageList,
        this.new_categories,
        this.location?.let {
            LocationResponseModel(
                it.latitude,
                it.longitude
            )
        },
        this.address

    )


    return item

}