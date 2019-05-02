package com.ronaker.app.ui.addProduct

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL

class AddProductImageAdapterViewModel : BaseViewModel() {
    private val productImage = MutableLiveData<String>()

   lateinit var  imageModel:Product.ProductImage




    fun bind(post: Product.ProductImage) {

        imageModel=post
        productImage.value= BASE_URL+post.url


    }

    fun getID(): String? {
        return imageModel.suid
    }

    fun getProductImage(): MutableLiveData<String> {
        return productImage
    }
}