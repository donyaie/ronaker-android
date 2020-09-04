package com.ronaker.app.ui.exploreProduct

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.databinding.AdapterProductCommentBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.nameFormat

class ProductCommentViewModel{
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

        comment.value = data.comment

        rate.value = data.stars

        data.user?.let {

            it.avatar?.let { avatar -> userAvatar.value = BASE_URL + avatar }
            userName.value = nameFormat (it.first_name ,it.last_name )

        }


    }


}