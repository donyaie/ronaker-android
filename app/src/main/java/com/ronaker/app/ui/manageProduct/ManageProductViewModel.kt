package com.ronaker.app.ui.manageProduct


import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManageProductViewModel(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    lateinit var mProduct: Product


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingAction: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val activeState: MutableLiveData<Boolean> = MutableLiveData()

    val productImage: MutableLiveData<String> = MutableLiveData()
    val productTitle: MutableLiveData<String> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()

    val regectedVisibility: MutableLiveData<Int> = MutableLiveData()
    val pendingVisibility: MutableLiveData<Int> = MutableLiveData()

    private var subscription: Disposable? = null
    private var updateActivesubscription: Disposable? = null

    var mSuid: String? = null

    fun loadProduct(product: Product) {
        mProduct = product
        mSuid = mProduct.suid
        fillProduct(product)
    }

    fun loadProduct(suid: String) {

        mSuid = suid
        subscription?.dispose()
        subscription = productRepository
            .getProduct( suid)

            .doOnSubscribe { //retry.value = null;
                loading.value = true
            }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.let {
                        mProduct = it
                        fillProduct(it)
                    }


                } else {
//                    retry.value = result.error?.message
                    errorMessage.value = result.error?.message
                }
            }


    }


    private fun fillProduct(product: Product) {
        productImage.value = BASE_URL + product.avatar
        productDescription.value = product.description
        productTitle.value = product.name



        activeState.value =
            Product.ActiveStatusEnum[product.user_status ?: ""] == Product.ActiveStatusEnum.Active


        when {
            Product.ReviewStatusEnum[product.review_status
                ?: ""] == Product.ReviewStatusEnum.Pending -> {
                pendingVisibility.value = View.VISIBLE
                regectedVisibility.value = View.GONE
            }
            Product.ReviewStatusEnum[product.review_status
                ?: ""] == Product.ReviewStatusEnum.Rejected -> {
                pendingVisibility.value = View.GONE
                regectedVisibility.value = View.VISIBLE
            }
            else -> {
                pendingVisibility.value = View.GONE
                regectedVisibility.value = View.GONE
            }
        }


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        updateActivesubscription?.dispose()
    }


    fun retry() {
        mSuid?.let { loadProduct(it) }
    }

    fun updateActiveState(active: Boolean) {

        updateActivesubscription?.dispose()
        val product = Product()
        product.user_status =
            if (active) Product.ActiveStatusEnum.Active.key else Product.ActiveStatusEnum.Deactive.key

        updateActivesubscription = productRepository
            .productUpdate( mSuid ?: "", product)

            .doOnSubscribe { loadingAction.value = true }
            .doOnTerminate { loadingAction.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {

                    activeState.value = active


                } else {
                    activeState.value = !active
                    errorMessage.value = result.error?.message
                }
            }


    }


}