package com.ronaker.app.ui.addProduct

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

class AddProductImageAdapterViewModel : BaseViewModel() {


    private val productImage = MutableLiveData<String>()

    lateinit var imageModel: Product.ProductImage

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun bind(post: Product.ProductImage, context: Context) {

        imageModel = post
        if (post.uri!=null)
            productImage.value = post.uri?.toString()
        else if(post.url!=null)
            productImage.value = BASE_URL + post.url

        imageModel.progress.observe(context as LifecycleOwner, Observer { state ->
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