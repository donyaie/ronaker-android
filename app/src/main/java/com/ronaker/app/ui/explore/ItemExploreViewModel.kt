package com.ronaker.app.ui.explore

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.extension.startActivityMakeSceneForResult
import com.ronaker.app.utils.toCurrencyFormat


class ItemExploreViewModel (val app: Application): BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Product
    var activity: AppCompatActivity?=null


    private lateinit var mBinder: AdapterExploreItemBinding

    fun bind(
        post: Product,
        binder: AdapterExploreItemBinding,
        context: AppCompatActivity?
    ) {
        data = post

        mBinder = binder
        activity = context
        productTitle.value = post.name

        when {
            post.price_per_day?:0!=0 -> {
                productPrice.value = String.format(
                    "%s %s",
                    post.price_per_day?.toCurrencyFormat(),
                    app.getString(
                        R.string.title_per_day
                    )
                )
            }
            post.price_per_week?:0!=0 -> {

                productPrice.value = String.format(
                    "%s %s",
                    post.price_per_week?.toCurrencyFormat(),
                    app.getString(
                        R.string.title_per_week
                    )
                )
            }
            post.price_per_month?:0!=0 -> {

                productPrice.value = String.format(
                    "%s %s",
                    post.price_per_month?.toCurrencyFormat(),
                    app.getString(
                        R.string.title_per_month
                    )
                )
            }
            else -> {
                productPrice.value = ""
            }
        }

        productImage.value = BASE_URL + post.avatar

        ViewCompat.setTransitionName(binder.image, post.avatar)

    }

    fun onClickProduct() {

            activity?.let { mActivity->

                mActivity.startActivityMakeSceneForResult(ExploreProductActivity.newInstance(mActivity,data),ExploreProductActivity.REQUEST_CODE)


            }

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