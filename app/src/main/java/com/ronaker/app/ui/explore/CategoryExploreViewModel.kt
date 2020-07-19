package com.ronaker.app.ui.explore

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterExploreCategoryBinding
import com.ronaker.app.model.Category
import com.ronaker.app.utils.BASE_URL


class CategoryExploreViewModel(val app: Application) : BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Category
    var activity: AppCompatActivity? = null


    private lateinit var mBinder: AdapterExploreCategoryBinding

    fun bind(
        post: Category,
        binder: AdapterExploreCategoryBinding,
        context: AppCompatActivity?
    ) {
        data = post

        mBinder = binder
        activity = context
        productTitle.value = post.title

        productImage.value = BASE_URL + post.avatar


        if (data.isSelected) {

            binder.title.setBackgroundResource(R.drawable.selector_corner_accent_fill)
            context?.let {

                binder.title.setTextColor(ContextCompat.getColor(it,R.color.colorTextLight))

            }
        } else {

            binder.title.setBackgroundResource(R.drawable.selector_corner_white_accent)
            context?.let {

                binder.title.setTextColor(ContextCompat.getColor(it,R.color.colorTextDark))

            }
        }


    }


    fun getCategoryTitle(): MutableLiveData<String> {
        return productTitle
    }


    fun getCategoryImage(): MutableLiveData<String> {
        return productImage
    }
}