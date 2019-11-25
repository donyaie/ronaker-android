package com.ronaker.app.ui.addProduct

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductImageAdapterViewModel ( var app: Application): BaseViewModel(app) {


    private val productImage = MutableLiveData<String>()

    lateinit var imageModel: Product.ProductImage

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun bind(post: Product.ProductImage) {

        imageModel = post
        if (post.uri!=null && post.uri.toString().isNotEmpty())
            productImage.value = post.uri?.toString()
        else if(!post.url.isNullOrEmpty())
            productImage.value = BASE_URL + post.url

        imageModel.progress.observe(app as LifecycleOwner, Observer { state ->
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