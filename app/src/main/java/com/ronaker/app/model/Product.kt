package com.ronaker.app.model

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.response.*
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product(
    var suid: String?
    , var name: String?
    , var price_per_day: Double?
    , var price_per_week: Double?
    , var price_per_month: Double?
    , var description: String?
    , var avatar: String?
    , var images: List<ProductImage>?
    , var categories: List<Category>?
    , var location: LatLng?
    , var address: String?
    , var avatar_suid: String? = null
    , var new_categories: ArrayList<String>? = null
    , var review_status: String? = null
    , var user_status: String? = null
    , var rate: Double? = null
    , var owner: User? = null
    , var isFavourite: Boolean? = null


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


    enum class ReviewStatusEnum constructor(key: String) {
        Pending("pending"),
        Accepted("accepted"),
        Rejected("rejected"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String): ReviewStatusEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }

    enum class ActiveStatusEnum constructor(key: String) {
        Active("active"),
        Deactive("deactive"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String): ActiveStatusEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }


    @Parcelize
    data class ProductImage(
        var url: String?,
        var suid: String?,
        var uri: Uri? = null,
        var isLocal: Boolean = false
    ) : Parcelable {

        constructor() : this(null, null)


        @IgnoredOnParcel
        val progress: MutableLiveData<Boolean> = MutableLiveData()

    }


    @Parcelize
    data class ProductRate(
        var stars: Float?,
        var comment: String?,
        var user: User?
    ) : Parcelable {


    }

}

fun List<ProductRatingResponceModel>.toProductRateList(): List<Product.ProductRate> {


    val list: ArrayList<Product.ProductRate> = ArrayList()

    this.forEach {

        val product = Product.ProductRate(it.stars, it.comment, it.user.toUserModel())

        list.add(product)
    }

    return list

}


fun List<ProductItemResponceModel>.toProductList(): List<Product> {


    val list: ArrayList<Product> = ArrayList()

    this.forEach {

        val product = it.toProduct()

        list.add(product)
    }

    return list

}


fun ProductItemResponceModel.toProduct(): Product {


    return Product(
        suid,
        name,
        price_per_day,
        price_per_week,
        price_per_month,
        description,
        avatar,
        images?.toProductImage() ?: ArrayList(),
        categories?.toCategoryList() ?: ArrayList(),
        if (location != null) LatLng(location.lat, location.lng) else null,
        address,
        null,
        null,
        review_status,
        user_status,
        rating
    )

}

fun ProductDetailResponceModel.toProductDetail(): Product {


    return Product(
        suid,
        name,
        price_per_day,
        price_per_week,
        price_per_month,
        description,
        avatar,
        images = images?.toProductImage() ?: ArrayList(),
        categories = categories?.toCategoryList() ?: ArrayList(),
        location = location?.let { LatLng(location.lat, location.lng) },
        address = address,
        user_status = user_status,
        review_status = review_status,
        rate = rating,
        owner = this.owner?.toUserModel(),
        isFavourite = this.favourite

    )

}


fun List<ProductItemImageResponceModel>.toProductImage(): List<Product.ProductImage> {


    val list: ArrayList<Product.ProductImage> = ArrayList()

    this.forEach {

        val product = Product.ProductImage(it.url, it.suid)
        product.isLocal = false

        list.add(product)
    }

    return list

}


fun Product.toProductCreateModel(): ProductCreateRequestModel {


    var imageList: ArrayList<String>? = ArrayList()


    if (this.images != null) {
        this.images?.forEach { it.suid?.let { suid -> imageList?.add(suid) } }

    }

    if (imageList?.size == 0) {
        imageList = null
    }


    return ProductCreateRequestModel(
        name,
        price_per_day,
        price_per_week,
        price_per_month,
        description,
        avatar_suid,
        imageList,
        new_categories,
        location?.let {
            LocationResponseModel(
                it.latitude,
                it.longitude
            )
        },
        address,
        user_status

    )

}