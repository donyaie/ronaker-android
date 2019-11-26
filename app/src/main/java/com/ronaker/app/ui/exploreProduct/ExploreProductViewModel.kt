package com.ronaker.app.ui.exploreProduct


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ExploreProductViewModel (app: Application): BaseViewModel(app) {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val checkout: MutableLiveData<String> = MutableLiveData()


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

      var mProduct:Product?=null

     var suid: String?=null


    private var subscription: Disposable? = null


    fun loadProduct(product: Product) {

        loading.value = false
        this.suid = product.suid
        mProduct=product
        fillProduct(product)

    }

    fun loadProduct(suid: String) {

        this.suid = suid
        subscription = productRepository
            .getProduct(userRepository.getUserToken(), suid)

            .doOnSubscribe {
                retry.value = null
                loading.value = true
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                   mProduct= result.data


                    mProduct?.let { fillProduct(it) }


                } else {
                    retry.value =  result.error?.message
//                    errorMessage.value = result.error?.detail
                }
            }


    }



    private fun fillProduct(product:Product){


        val images=ArrayList<String>()
        product.images?.forEach {
            images.add( BASE_URL+it.url)
        }
        imageList.value=images

        productImage.value = BASE_URL + product.avatar
        productDescription.value = product.description
        productTitle.value = product.name

        productAddress.value=product.address

        productLocation.value=product.location


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


    fun onRetry(){
        suid?.let { loadProduct(it) }
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}