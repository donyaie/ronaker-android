package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.CategoriesResponseModel
import com.ronaker.app.data.network.response.ProductItemImageResponceModel
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
data class Category(
    var suid: String
    , var title: String
    , var avatar: String
    , var sub_categories: List<Category>?
): Parcelable {


}


fun List<CategoriesResponseModel>.toCategoryList(): List<Category> {


    var list: ArrayList<Category> = ArrayList()

    this.forEach {

        var value = Category(it.suid, it.title,it.avatar,ArrayList())


        if(it.sub_categories!=null && it.sub_categories.isNotEmpty() )
            value.sub_categories=it.sub_categories.toCategoryList()
        list.add(value)
    }

    return list

}

