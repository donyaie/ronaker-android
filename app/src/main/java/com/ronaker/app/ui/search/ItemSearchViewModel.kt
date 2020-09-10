package com.ronaker.app.ui.search

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.databinding.AdapterSearchItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import com.ronaker.app.utils.toCurrencyFormat


class ItemSearchViewModel {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productPricePostfix = MutableLiveData<String>()


    private val productImage = MutableLiveData<String>()
    private val productAddress = MutableLiveData<String>()

    lateinit var data: Product


    fun bind(
        post: Product, binding: AdapterSearchItemBinding
    ) {
        data = post

        productTitle.value = post.name

        productAddress.postValue(post.address)

        val context = binding.root.getParentActivity()?.baseContext?: binding.root.getApplication()

        when {
            post.price_per_day ?: 0 != 0 -> {

                productPrice.value = post.price_per_day?.toCurrencyFormat()
                productPricePostfix.value = context.getString(R.string.title_pers_day)

            }
            post.price_per_week ?: 0 != 0 -> {


                productPrice.value = post.price_per_week?.toCurrencyFormat()
                productPricePostfix.value = context.getString(R.string.title_pers_week)

            }
            post.price_per_month ?: 0 != 0 -> {


                productPrice.value = post.price_per_month?.toCurrencyFormat()
                productPricePostfix.value = context.getString(R.string.title_pers_month)


            }
            else -> {
                productPrice.value = ""

                productPricePostfix.value =""
            }


        }

        productImage.value = BASE_URL + post.avatar


    }


    fun getProductPricePostfix(): MutableLiveData<String> {
        return productPricePostfix
    }

    fun getProductTitle(): MutableLiveData<String> {
        return productTitle
    }

    fun getProductPrice(): MutableLiveData<String> {
        return productPrice
    }

    fun getProductAddress(): MutableLiveData<String> {
        return productAddress
    }

    fun getProductImage(): MutableLiveData<String> {
        return productImage
    }
}