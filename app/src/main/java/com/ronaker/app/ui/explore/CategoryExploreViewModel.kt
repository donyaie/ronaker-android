package com.ronaker.app.ui.explore

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.databinding.AdapterExploreCategoryBinding
import com.ronaker.app.model.Category
import com.ronaker.app.utils.BASE_URL
import javax.inject.Inject


class CategoryExploreViewModel( app: Application) : BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Category

    @Inject
    lateinit var resourcesRepository: ResourcesRepository


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

            binder.title.setBackgroundResource(R.drawable.selector_corner_accent_fill)


            binder.title.setTextColor( resourcesRepository.getColor(R.color.colorTextLight))



        } else {

            binder.title.setBackgroundResource(R.drawable.selector_corner_white_accent)
            binder.title.setTextColor( resourcesRepository.getColor(R.color.colorTextDark))
        }


    }


    fun getCategoryTitle(): MutableLiveData<String> {
        return productTitle
    }


    fun getCategoryImage(): MutableLiveData<String> {
        return productImage
    }
}