package com.ronaker.app.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.response.*

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(
    var suid: String? = null
    , var name: String? = null
    , var price_per_day: Double? = null
    , var price_per_week: Double? = null
    , var price_per_month: Double? = null
    , var description: String? = null
    , var avatar: String? = null
    , var images: List<Image> = ArrayList()
    , var categories: List<Category>? = null
    , var location: LatLng? = null
    , var address: String? = null
    , var insurance_image: String? = null
    , var avatar_suid: String? = null
    , var new_categories: ArrayList<String>? = null
    , var new_insurance_image_suid: String? = null
    , var review_status: String? = null
    , var user_status: String? = null
    , var rate: Double? = null
    , var owner: User? = null
    , var isFavourite: Boolean? = null


) : Parcelable {

    override fun equals(other: Any?): Boolean {

        if (other is Product) {
            return this.hashCode() == other.hashCode()
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = suid?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (price_per_day?.hashCode() ?: 0)
        result = 31 * result + (price_per_week?.hashCode() ?: 0)
        result = 31 * result + (price_per_month?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + (images.hashCode() )
        result = 31 * result + (categories?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (avatar_suid?.hashCode() ?: 0)
        result = 31 * result + (new_categories?.hashCode()?:0)
        result = 31 * result + (review_status?.hashCode() ?: 0)
        result = 31 * result + (user_status?.hashCode() ?: 0)
        result = 31 * result + (rate?.hashCode()?: 0)
        result = 31 * result + (owner?.hashCode() ?: 0)
        result = 31 * result + (isFavourite?.hashCode() ?: 0)
        return result
    }


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
    data class ProductRate(
        var stars: Float?,
        var comment: String?,
        var user: User?
    ) : Parcelable

}

fun List<ProductRatingResponceModel>.toProductRateList(): List<Product.ProductRate> {


    val list: ArrayList<Product.ProductRate> = ArrayList()

    this.forEach {

        val product = Product.ProductRate(it.stars, it.comment, it.user.toUserModel())

        list.add(product)
    }

    return list

}


fun List<ProductItemResponseModel>.toProductList(): List<Product> {


    val list: ArrayList<Product> = ArrayList()

    this.forEach {

        val product = it.toProduct()

        list.add(product)
    }

    return list

}


fun ProductItemResponseModel.toProduct(): Product {


    return Product(
        suid = suid,
        name = name,
        price_per_day = price_per_day,
        price_per_week = price_per_week,
        price_per_month = price_per_month,
        description = description,
        avatar = avatar,
        images = images?.toProductImage() ?: ArrayList(),
        categories = categories?.toCategoryList() ?: ArrayList(),
        location = if (location != null) LatLng(location.lat, location.lng) else null,
        address = address,
        review_status = review_status,
        user_status = user_status,
        rate = rating,
        isFavourite = is_favourite
    )

}

fun ProductDetailResponseModel.toProductDetail(): Product {

    return Product(
        suid = suid,
        name = name,
        price_per_day = price_per_day,
        price_per_week = price_per_week,
        price_per_month = price_per_month,
        description = description,
        avatar = avatar,
        images = images?.toProductImage() ?: ArrayList(),
        categories = categories?.toCategoryList() ?: ArrayList(),
        location = location?.let { LatLng(location.lat, location.lng) },
        address = address,
        user_status = user_status,
        review_status = review_status,
        rate = rating,
        owner = this.owner?.toUserModel(),
        isFavourite = this.is_favourite,
        insurance_image = this.insurance_image

    )

}


fun List<ProductItemImageResponceModel>.toProductImage(): List<Image> {


    val list: ArrayList<Image> = ArrayList()

    this.forEach {

        val product = Image(it.url, it.suid)
        product.isLocal = false

        list.add(product)
    }

    return list

}


fun Product.toProductCreateModel(): ProductCreateRequestModel {


    var imageList: ArrayList<String>? = ArrayList()


    this.images.forEach { it.suid?.let { suid -> imageList?.add(suid) } }



    if (imageList.isNullOrEmpty()) {
        imageList = null
    }


    return ProductCreateRequestModel(
        name = name,
        price_per_day = price_per_day,
        price_per_week = price_per_week,
        price_per_month = price_per_month,
        description = description,
        new_avatar_suid = avatar_suid,
        new_images = imageList,
        new_categories = new_categories,
        location = location?.let {
            LocationResponseModel(
                it.latitude,
                it.longitude
            )
        },
        address = address,
        user_status = user_status,
        new_insurance_image_suid = new_insurance_image_suid

    )

}