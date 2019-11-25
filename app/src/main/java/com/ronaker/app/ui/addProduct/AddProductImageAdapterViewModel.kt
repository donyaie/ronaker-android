package com.ronaker.app.ui.addProduct

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL

class AddProductImageAdapterViewModel ( var app: Application): BaseViewModel(app) {


    private val productImage = MutableLiveData<String>()

    lateinit var imageModel: Product.ProductImage

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun bind(
        post: Product.ProductImage,
        baseActivity: BaseActivity
    ) {

        imageModel = post
        if (post.uri!=null && post.uri.toString().isNotEmpty())
            productImage.value = post.uri?.toString()
        else if(!post.url.isNullOrEmpty())
            productImage.value = BASE_URL + post.url

        imageModel.progress.observe(baseActivity, Observer { state ->
            if (state) loadingVisibility.value = View.VISIBLE
            else
                loadingVisibility.value = View.GONE

        })


    }


    fun getImage(): Product.ProductImage? {
        return imageModel
    }

    fun getProductImage(): MutableLiveData<String> {
        return productImage
    }
}