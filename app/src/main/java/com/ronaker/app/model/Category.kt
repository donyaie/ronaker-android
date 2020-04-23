package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.CategoriesResponseModel
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Category(
    var suid: String
    , var title: String
    , var avatar: String
    , var sub_categories: List<Category>?
): Parcelable{
    @IgnoredOnParcel
    var isSelected=false
}


fun List<CategoriesResponseModel>.toCategoryList(): List<Category> {


    val list: ArrayList<Category> = ArrayList()

    this.forEach {

        val value = Category(it.suid, it.title,it.avatar,ArrayList())


        if(it.sub_categories!=null && it.sub_categories.isNotEmpty() )
            value.sub_categories=it.sub_categories.toCategoryList()
        list.add(value)
    }

    return list

}

