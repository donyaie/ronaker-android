package com.ronaker.app.ui.exploreProduct


import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.actionOpenProduct
import com.ronaker.app.utils.toCurrencyFormat
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class ExploreProductViewModel(app: Application) : BaseViewModel(app) {

    private val TAG = ExploreProductViewModel::class.java.simpleName

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
    var resourcesRepository: ResourcesRepository



    init {


        rateListAdapter = ProductCommentAdapter(dataList)
    }

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingComment: MutableLiveData<Boolean> = MutableLiveData()
    val loadingRefresh: MutableLiveData<Boolean> = MutableLiveData()
    val noCommentVisibility: MutableLiveData<Int> = MutableLiveData()


    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val stratTransition: MutableLiveData<Boolean> = MutableLiveData()

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

    var mSuid: String? = null


    private var subscription: Disposable? = null
    private var commentSubscription: Disposable? = null
    private var faveSubscription: Disposable? = null

    fun loadProduct(suid: String) {
        uiScope.launch {
            loadProduct(suid, true)
        }
    }

    fun loadProduct(product: Product) {
        uiScope.launch {
            val time1 = measureTimeMillis {

                loading.postValue(false)
                mSuid = product.suid
                mProduct = product
                fillProduct(product)
            }

            AppDebug.log(TAG, "fillProduct time : $time1")
            val time2 = measureTimeMillis {


                product.suid?.let {
                    loadProduct(it, false)
                }
            }

            AppDebug.log(TAG, "loadProduct time : $time2")
        }
    }

    suspend fun loadProduct(id: String, showLoading: Boolean, refresh: Boolean = false) =
        withContext(Dispatchers.IO) {

            mSuid = id
            subscription?.dispose()

            subscription = productRepository
                .getProduct( id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())

                .doOnSubscribe {
                    retry.postValue(null)
                    loading.postValue(showLoading)

                    loadingRefresh.postValue(refresh)

                }
                .doOnTerminate {
                    loading.postValue(false)
                    loadingRefresh.postValue(false)
                }

                .subscribe { result ->
                    if (result.isSuccess()) {

                        mProduct = result.data


                        mProduct?.let { fillProduct(it) }


                    } else {
                        if (showLoading)
                            retry.postValue(result.error?.message)
                        else {
                            errorMessage.postValue(result.error?.message)
                        }
//
                    }

                }


            loadComment(id)
        }

    suspend fun loadComment(suid: String) =
        withContext(Dispatchers.IO) {


            commentSubscription?.dispose()
            commentSubscription = productRepository
                .getProductRating( suid)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {

                    loadingComment.postValue(true)
                }
                .doOnTerminate {

                    loadingComment.postValue(false)
                }

                .subscribe { result ->
                    if (result.isSuccess()) {


                        if (result.data?.results != null && result.data.results.isNotEmpty()) {
                            noCommentVisibility.postValue(View.GONE)
                        } else {

                            noCommentVisibility.postValue(View.VISIBLE)
                        }

                        result.data?.results?.let {
                            dataList.clear()
                            dataList.addAll(it)

                            rateListAdapter.updateList()
                        }


                    }

                }

        }


    private fun fillProduct(product: Product) {

        stratTransition.postValue(true)

        getAnalytics()?.actionOpenProduct(
            product.suid,
            product.name,
            if (product.categories?.size ?: 0 > 0) product.categories?.get(0)?.title else null
        )

        val images = ArrayList<String>()
        product.images.forEach {
            images.add(BASE_URL + it.url)
        }
        imageList.postValue(images)

        productImage.postValue(BASE_URL + product.avatar)

        product.owner?.let {

            it.avatar?.let { avatar ->

                userImage.postValue(BASE_URL + avatar)

            }

            userName.postValue((it.first_name ?: "") + " " + (it.last_name ?: ""))

            userRepository.getUserInfo()?.let { info ->
                if (it.suid?.compareTo(info.suid ?: "") == 0)
                    checkoutVisibility.postValue(View.GONE)
                else
                    checkoutVisibility.postValue(View.VISIBLE)
            }

        }
        //title_positive_rate

        isFavorite.postValue(product.isFavourite)

        product.rate?.let { rate ->


            productRate.postValue(rate.toString())

            productRatestatus.postValue("Positive Rate")


        } ?: run {

            productRate.postValue("")
            productRatestatus.postValue("Not Rated")
        }


        productDescription.postValue(product.description)
        productTitle.postValue(product.name)

        productAddress.postValue(product.address)

        productLocation.postValue(product.location)




        when {
            product.price_per_day != 0.0 -> {

                productPrice.postValue(product.price_per_day?.toCurrencyFormat())
                productPriceTitle.postValue(resourcesRepository.getString(R.string.title_per_day))
            }
            product.price_per_week != 0.0 -> {

                productPrice.postValue(product.price_per_week?.toCurrencyFormat())

                productPriceTitle.postValue(resourcesRepository.getString(R.string.title_per_week))
            }
            product.price_per_month != 0.0 -> {

                productPrice.postValue(product.price_per_month?.toCurrencyFormat())

                productPriceTitle.postValue(resourcesRepository.getString(R.string.title_per_month))
            }
        }


    }


    fun checkOut() {

        checkout.postValue(mSuid)
    }


    fun onRefresh() {

        uiScope.launch {
            mSuid?.let {
                loadProduct(it, showLoading = false, refresh = true)
            }
        }
    }

    fun onRetry() {

        uiScope.launch {
            mSuid?.let {
                loadProduct(it, showLoading = true)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        commentSubscription?.dispose()
        faveSubscription?.dispose()
    }

    fun setFavorite(suid: String) {

        faveSubscription?.dispose()

        if (mProduct?.isFavourite != null && mProduct?.isFavourite == true) {

            faveSubscription = productRepository
                .productSavedRemove( suid)

                .doOnSubscribe {

//                    loading.postValue( true
                }
                .doOnTerminate {

//                    loading.postValue( false
                }

                .subscribe { result ->
                    if (result.isAcceptable()) {
                        mProduct?.isFavourite = false

                        isFavorite.postValue(false)


                    } else {
                        errorMessage.postValue(result?.error?.message)
                    }
                }

        } else {
            faveSubscription = productRepository
                .productSave( suid)

                .doOnSubscribe {

//                    loading.postValue( true
                }
                .doOnTerminate {

//                    loading.postValue( false
                }

                .subscribe { result ->
                    if (result.isAcceptable()) {

                        mProduct?.isFavourite = true
                        isFavorite.postValue(true)


                    } else {
                        errorMessage.postValue(result?.error?.message)
                    }
                }
        }


    }


}