package com.ronaker.app.ui.explore

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL

class ItemExploreViewModel : BaseViewModel() {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    fun bind(post: Product) {
        productTitle.value = post.name

        if (!(post.price_per_day?.equals(0) ?: (false))) {
            productPrice.value = "€" + post.price_per_day + " per day"
        } else if (!(post.price_per_week?.equals(0) ?: (false))) {

            productPrice.value = "€" + post.price_per_week + " per week"
        } else if (!(post.price_per_month?.equals(0) ?: (false))) {

            productPrice.value = "€" + post.price_per_month + " per month"
        }
        else{
            productPrice.value=""
        }

        productImage.value= BASE_URL+post.avatar


    }

    fun getProductTitle(): MutableLiveData<String> {
        return productTitle
    }

    fun getProductPrice(): MutableLiveData<String> {
        return productPrice
    }

    fun getProductImage(): MutableLiveData<String> {
        return productImage
    }
}