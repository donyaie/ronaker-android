package com.ronaker.app.ui.addProduct

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.model.Image
import com.ronaker.app.utils.BASE_URL

class AddProductImageAdapterViewModel {


    private val productImage = MutableLiveData<String>()

    lateinit var imageModel: Image

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val selectDefaultVisibility: MutableLiveData<Int> = MutableLiveData()


    fun bind(
        post: Image,
        baseActivity: AppCompatActivity?
    ) {

        imageModel = post
        if (post.uri != null && post.uri.toString().isNotEmpty())
            productImage.value = post.uri?.toString()
        else if (!post.url.isNullOrEmpty())
            productImage.value = BASE_URL + post.url

        baseActivity?.let {
            imageModel.progress.observe(it, { state ->
                if (state)
                    loadingVisibility.value = View.VISIBLE
                else
                    loadingVisibility.value = View.GONE

            })
        }


        if(imageModel.isSelected)
            selectDefaultVisibility.postValue(View.VISIBLE)
        else
            selectDefaultVisibility.postValue(View.GONE)









    }


    fun getImage(): Image {
        return imageModel
    }

    fun getProductImage(): MutableLiveData<String> {
        return productImage
    }
}