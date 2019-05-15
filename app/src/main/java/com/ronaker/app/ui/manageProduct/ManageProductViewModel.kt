package com.ronaker.app.ui.manageProduct


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
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


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val   productImage: MutableLiveData<String> = MutableLiveData()
    val   productTitle: MutableLiveData<String> = MutableLiveData()
    val   productDescription: MutableLiveData<String> = MutableLiveData()


    private  var subscription: Disposable?=null

    init {

    }

    fun loadProduct(suid: String) {


        subscription = productRepository
            .getProduct(userRepository.getUserToken(), suid)

            .doOnSubscribe { loading.value=true}
            .doOnTerminate {loading.value=false}
            .subscribe { result ->
                if (result.isSuccess()) {
                    productImage.value= BASE_URL + result.data?.avatar
                    productDescription.value=result.data?.description
                    productTitle.value=result.data?.name
                } else {
                   errorMessage.value=result.error?.detail
                }
            }


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}