package com.ronaker.app.ui.exploreProduct


import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.ui.explore.ItemExploreAdapter
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.actionOpenProduct
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ExploreProductViewModel(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit
    var productRepository: ProductRepository




    var dataList: ArrayList<Product.ProductRate> = ArrayList()

    var rateListAdapter: ProductCommentAdapter

    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    init {

        rateListAdapter = ProductCommentAdapter(dataList)
    }

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val checkout: MutableLiveData<String> = MutableLiveData()

    val productRatestatus: MutableLiveData<String> = MutableLiveData()
    val productRate: MutableLiveData<String> = MutableLiveData()


    val checkoutVisibility: MutableLiveData<Int> = MutableLiveData()

    val userName: MutableLiveData<String> = MutableLiveData()
    val userImage: MutableLiveData<String> = MutableLiveData()


    val imageList: MutableLiveData<ArrayList<String>> = MutableLiveData()


    val productImage: MutableLiveData<String> = MutableLiveData()
    val productTitle: MutableLiveData<String> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()

    val productPrice: MutableLiveData<String> = MutableLiveData()
    val productPriceTitle: MutableLiveData<String> = MutableLiveData()
    val productLocation: MutableLiveData<LatLng> = MutableLiveData()
    val productAddress: MutableLiveData<String> = MutableLiveData()

    var mProduct: Product? = null

    var suid: String? = null


    private var subscription: Disposable? = null
    private var commentSubscription: Disposable? = null


    fun loadProduct(product: Product) {

        loading.value = false
        this.suid = product.suid
        mProduct = product
        fillProduct(product)

        product.suid?.let { loadProduct(it, false) }

    }

    fun loadProduct(suid: String, showLoading: Boolean) {
        subscription?.dispose()
        this.suid = suid
        subscription = productRepository
            .getProduct(userRepository.getUserToken(), suid)

            .doOnSubscribe {
                retry.value = null
                loading.value = showLoading
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    mProduct = result.data


                    mProduct?.let { fillProduct(it) }


                } else {
                    if (showLoading)
                        retry.value = result.error?.message
                    else {
                        errorMessage.value = result.error?.message
                    }
//
                }
            }
        loadComment(suid)

    }

    fun loadComment(suid: String){


        commentSubscription?.dispose()
        commentSubscription=productRepository
            .getProductRating(userRepository.getUserToken(), suid)

            .doOnSubscribe {

            }
            .doOnTerminate {

            }

            .subscribe { result ->
                if (result.isSuccess()) {


                    result.data?.results?.let {

                        dataList.clear()
                        dataList.addAll(it)
                        rateListAdapter.notifyDataSetChanged()
                    }


                }

            }

    }


    private fun fillProduct(product: Product) {

        getAnalytics()?.actionOpenProduct(
            product.suid,
            product.name,
            if (product.categories?.size ?: 0 > 0) product.categories?.get(0)?.title else null
        )

        val images = ArrayList<String>()
        product.images?.forEach {
            images.add(BASE_URL + it.url)
        }
        imageList.value = images

        productImage.value = BASE_URL + product.avatar

        product.owner?.let {

            it.avatar?.let {avatar->

                userImage.value=BASE_URL+avatar

            }

            userName.value=(it.first_name?:"")+" "+(it.last_name?:"")

            userRepository.getUserInfo()?.let {info->
                if(it.suid?.compareTo(info.suid?:"")==0)
                    checkoutVisibility.value= View.GONE
                else
                    checkoutVisibility.value= View.VISIBLE
            }

        }
        //title_positive_rate

        product.rate?.let {rate->


            productRate.value=rate.toString()

            productRatestatus.value="Positive Rate"


        }?: run {

            productRate.value=""
            productRatestatus.value="Not Rated"
        }


        productDescription.value = product.description
        productTitle.value = product.name

        productAddress.value = product.address

        productLocation.value = product.location




        when {
            product.price_per_day != 0.0 -> {

                productPrice.value = String.format(
                    "%s%.02f",
                    context.getString(R.string.title_curency_symbol),
                    product.price_per_day
                )
                productPriceTitle.value = context.getString(R.string.title_per_day)
            }
            product.price_per_week != 0.0 -> {

                productPrice.value = String.format(
                    "%s%.02f", context.getString(R.string.title_curency_symbol),
                    product.price_per_week
                )
                productPriceTitle.value = context.getString(R.string.title_per_week)
            }
            product.price_per_month != 0.0 -> {

                productPrice.value = String.format(
                    "%s%.02f", context.getString(R.string.title_curency_symbol),
                    product.price_per_month
                )
                productPriceTitle.value = context.getString(R.string.title_per_month)
            }
        }


    }


    fun checkOut() {

        checkout.value = suid
    }


    fun onRefresh() {

        suid?.let { loadProduct(it, false) }
    }

    fun onRetry() {
        suid?.let { loadProduct(it, true) }
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        commentSubscription?.dispose()
    }


}