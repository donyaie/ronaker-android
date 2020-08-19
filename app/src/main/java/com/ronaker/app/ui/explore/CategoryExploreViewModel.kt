package com.ronaker.app.ui.explore

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreCategoryBinding
import com.ronaker.app.model.Category
import com.ronaker.app.utils.BASE_URL

class CategoryExploreViewModel {
    private val productTitle = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Category


    private lateinit var mBinder: AdapterExploreCategoryBinding

    fun bind(
        post: Category,
        binder: AdapterExploreCategoryBinding
    ) {
        data = post

        mBinder = binder
        productTitle.value = post.title

        productImage.value = BASE_URL + post.avatar


        if (data.isSelected) {
//
            binder.shadow.setBackgroundResource(R.drawable.selector_corner_accent_transparent)
//
//
//            binder.title.setTextColor( resourcesRepository.getColor(R.color.colorTextLight))
//
//
//
//            ShapeDrawableHelper.changeSvgDrawableColor(binder.root.context,R.color.colorTextLight,binder.image)


        } else {
//
            binder.shadow.setBackgroundResource(R.drawable.selector_corner_black_transparent)
//            binder.title.setTextColor( resourcesRepository.getColor(R.color.colorTextDark))
//
//
//            ShapeDrawableHelper.changeSvgDrawableColor(binder.root.context,R.color.colorTextDark,binder.image)
        }


    }


    fun getCategoryTitle(): MutableLiveData<String> {
        return productTitle
    }


    fun getCategoryImage(): MutableLiveData<String> {
        return productImage
    }
}