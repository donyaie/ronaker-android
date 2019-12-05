package com.ronaker.app.ui.exploreProduct

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterProductCommentBinding
import com.ronaker.app.model.Product

class ProductCommentViewModel (val app: Application): BaseViewModel(app) {
     val userAvatar = MutableLiveData<String>()
     val userName = MutableLiveData<String>()
     val rate = MutableLiveData<Float>()
     val comment = MutableLiveData<String>()



    lateinit var data: Product.ProductRate


    private lateinit var mBinder: AdapterProductCommentBinding

    fun bind(
        post: Product.ProductRate,
        binder: AdapterProductCommentBinding
    ) {
        data = post
        mBinder = binder

        comment.value=  data.comment

        rate.value=  data.stars




    }


}