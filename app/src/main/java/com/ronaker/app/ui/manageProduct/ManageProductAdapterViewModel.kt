package com.ronaker.app.ui.manageProduct

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterManageProductBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import com.ronaker.app.utils.toCurrencyFormat

class ManageProductAdapterViewModel {
    private val productTitle = MutableLiveData<String?>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()


    private val activeVisibility = MutableLiveData<Int>()
    private val pendingVisibility = MutableLiveData<Int>()
    private val deactiveVisibility = MutableLiveData<Int>()
    private val rejectedVisibility = MutableLiveData<Int>()


    fun bind(post: Product, binding: AdapterManageProductBinding) {
        productTitle.value = post.name


        val context = binding.root.getParentActivity()?.baseContext ?: binding.root.getApplication()
        when {
            post.price_per_day ?: 0 != 0 -> productPrice.value = String.format(
                "%s %s",
                post.price_per_day?.toCurrencyFormat(),
                context.getString(
                    R.string.title_per_day
                )
            )
            post.price_per_week ?: 0 != 0 -> productPrice.value = String.format(
                "%s %s",
                post.price_per_week?.toCurrencyFormat(),
                context.getString(
                    R.string.title_per_week
                )
            )
            post.price_per_month ?: 0 != 0 -> productPrice.value = String.format(
                "%s %s",
                post.price_per_month?.toCurrencyFormat(),
                context.getString(
                    R.string.title_per_month
                )
            )
            else -> productPrice.value = ""
        }
        if (Product.ReviewStatusEnum[post.review_status
                ?: ""] == Product.ReviewStatusEnum.Rejected
        ) {
            activeVisibility.postValue(View.GONE)
            deactiveVisibility.postValue(View.GONE)
            pendingVisibility.postValue(View.GONE)
            rejectedVisibility.postValue(View.VISIBLE)
        } else if (Product.ReviewStatusEnum[post.review_status
                ?: ""] == Product.ReviewStatusEnum.Pending
        ) {
            activeVisibility.postValue(View.GONE)
            deactiveVisibility.postValue(View.GONE)
            pendingVisibility.postValue(View.VISIBLE)
            rejectedVisibility.postValue(View.GONE)
        } else if (Product.ActiveStatusEnum[post.user_status
                ?: ""] == Product.ActiveStatusEnum.Active
        ) {
            activeVisibility.postValue(View.VISIBLE)
            deactiveVisibility.postValue(View.GONE)
            pendingVisibility.postValue(View.GONE)


            rejectedVisibility.postValue(View.GONE)
        } else if (Product.ActiveStatusEnum[post.user_status
                ?: ""] == Product.ActiveStatusEnum.Deactive
        ) {
            activeVisibility.postValue(View.GONE)
            deactiveVisibility.postValue(View.VISIBLE)

            pendingVisibility.postValue(View.GONE)
            rejectedVisibility.postValue(View.GONE)
        }


        productImage.postValue(BASE_URL + post.avatar)


    }


    fun getProductTitle(): MutableLiveData<String?> {
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

    fun getPendingVisibility(): MutableLiveData<Int> {
        return pendingVisibility
    }

    fun getRejectedVisibility(): MutableLiveData<Int> {
        return rejectedVisibility
    }


    fun getDeactiveVisibility(): MutableLiveData<Int> {
        return deactiveVisibility
    }
}