package com.ronaker.app.ui.explore

import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.BASE_URL


class ItemExploreViewModel : BaseViewModel() {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()

    lateinit var data: Product
    lateinit var activity: DashboardActivity


    lateinit var mBinder: AdapterExploreItemBinding

    fun bind(
        post: Product,
        binder: AdapterExploreItemBinding,
        context: DashboardActivity
    ) {
        data = post

        mBinder = binder
        activity = context
        productTitle.value = post.name

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

        ViewCompat.setTransitionName(binder.image, post.avatar);

    }

    fun onClickProduct() {


        val options = ViewCompat.getTransitionName( mBinder.image)?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                mBinder.image,
                it
            )
        }

        activity.startActivity(ExploreProductActivity.newInstance(activity, data,ViewCompat.getTransitionName( mBinder.image)),options?.toBundle())
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