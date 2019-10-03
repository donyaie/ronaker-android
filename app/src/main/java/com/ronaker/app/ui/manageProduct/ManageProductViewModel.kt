package com.ronaker.app.ui.manageProduct


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.model.toProductDetail
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManageProductViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    lateinit var mProduct: Product


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()


    val productImage: MutableLiveData<String> = MutableLiveData()
    val productTitle: MutableLiveData<String> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null

    init {

    }


    fun loadProduct(product: Product) {
        loading.value = false
        mProduct = product
        fillProduct(product)
    }

    fun loadProduct(suid: String) {


        subscription = productRepository
            .getProduct(userRepository.getUserToken(), suid)

            .doOnSubscribe { retry.value = null; loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.toProductDetail()?.let {
                        mProduct = it
                        fillProduct(it)
                    }


                } else {
                    retry.value = result.error?.detail;
                   // errorMessage.value = result.error?.detail
                }
            }


    }


    private fun fillProduct(product: Product) {
        productImage.value = BASE_URL + product.avatar
        productDescription.value = product.description
        productTitle.value = product.name
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}