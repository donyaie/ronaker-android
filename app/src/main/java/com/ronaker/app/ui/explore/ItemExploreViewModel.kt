package com.ronaker.app.ui.explore

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Product
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.ui.exploreProduct.ExploreProductFragment
import com.ronaker.app.utils.BASE_URL

class ItemExploreViewModel : BaseViewModel() {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Product
    lateinit var   activity: DashboardActivity

    fun bind(post: Product, context: DashboardActivity) {
        data = post
        productTitle.value = post.name
        activity=context
        if (!(post.price_per_day?.equals(0) ?: (false))) {
            productPrice.value = "€" + post.price_per_day + " per day"
        } else if (!(post.price_per_week?.equals(0) ?: (false))) {

            productPrice.value = "€" + post.price_per_week + " per week"
        } else if (!(post.price_per_month?.equals(0) ?: (false))) {

            productPrice.value = "€" + post.price_per_month + " per month"
        } else {
            productPrice.value = ""
        }

        productImage.value = BASE_URL + post.avatar


    }

    fun onClickProduct() {


        activity.startActivity(data.suid?.let { ExploreProductActivity.newInstance(activity, it) })
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