package com.ronaker.app.ui.manageProduct

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.toCurrencyFormat
import javax.inject.Inject

class ManageProductAdapterViewModel(app: Application) : BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()



    private val activeVisibility = MutableLiveData<Int>()
    private val deactiveVisibility = MutableLiveData<Int>()

    lateinit var data: Product

    @Inject
    lateinit var resourcesRepository: ResourcesRepository


    fun bind(post: Product) {
        data = post
        productTitle.value = post.name
        when {
            post.price_per_day ?: 0 != 0 -> productPrice.value = String.format(
                "%s %s",
                post.price_per_day?.toCurrencyFormat(),
                resourcesRepository.getString(
                    R.string.title_per_day
                )
            )
            post.price_per_week ?: 0 != 0 -> productPrice.value = String.format(
                "%s %s",
                post.price_per_week?.toCurrencyFormat(),
                resourcesRepository.getString(
                    R.string.title_per_week
                )
            )
            post.price_per_month ?: 0 != 0 -> productPrice.value = String.format(
                "%s %s",
                post.price_per_month?.toCurrencyFormat(),
                resourcesRepository.getString(
                    R.string.title_per_month
                )
            )
            else -> productPrice.value = ""
        }


        if (Product.ActiveStatusEnum[data.user_status ?: ""] == Product.ActiveStatusEnum.Active) {
            activeVisibility.value = View.VISIBLE
            deactiveVisibility.value = View.GONE
        } else {
            activeVisibility.value = View.GONE
            deactiveVisibility.value = View.VISIBLE
        }


        productImage.value = BASE_URL + post.avatar


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

    fun getActiveVisibility(): MutableLiveData<Int> {
        return activeVisibility
    }

    fun getDeactiveVisibility(): MutableLiveData<Int> {
        return deactiveVisibility
    }
}