package com.ronaker.app.ui.explore

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterExploreCategoryBinding
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.extension.startActivityMakeScene
import com.ronaker.app.utils.extension.startActivityMakeSceneForResult
import com.ronaker.app.utils.toCurrencyFormat


class CategoryExploreViewModel (val app: Application): BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Category
    var activity: AppCompatActivity?=null


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


    }

    fun onClickProduct() {


    }

    fun getCategoryTitle(): MutableLiveData<String> {
        return productTitle
    }


    fun getCategoryImage(): MutableLiveData<String> {
        return productImage
    }
}