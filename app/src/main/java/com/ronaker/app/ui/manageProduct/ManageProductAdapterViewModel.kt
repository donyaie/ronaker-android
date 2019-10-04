package com.ronaker.app.ui.manageProduct

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Product
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.BASE_URL

class ManageProductAdapterViewModel : BaseViewModel() {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Product
    lateinit var activity: DashboardActivity

    fun bind(post: Product, context: DashboardActivity) {
        data = post
        productTitle.value = post.name
        activity = context
        if (post.price_per_day?.equals(0) != true) {
            productPrice.value = String.format(
                "%s%.02f %s", context.getString(R.string.title_curency_symbol), post.price_per_day, context.getString(
                    R.string.title_per_day
                )
            )
        } else if (post.price_per_week?.equals(0) != true) {

            productPrice.value = String.format(
                "%s%.02f %s", context.getString(R.string.title_curency_symbol), post.price_per_week, context.getString(
                    R.string.title_per_week
                )
            )
        } else if (post.price_per_month?.equals(0) != true) {

            productPrice.value = String.format(
                "%s%.02f %s", context.getString(R.string.title_curency_symbol), post.price_per_month, context.getString(
                    R.string.title_per_month
                )
            )
        } else {
            productPrice.value = ""
        }

        productImage.value = BASE_URL + post.avatar


    }

    fun onClickProduct() {

        activity.pushFragment(ManageProductFragment.newInstance(data))
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