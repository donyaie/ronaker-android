package com.ronaker.app.ui.orderMessage


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class OrderMessageViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()



    val productPrice: MutableLiveData<String> = MutableLiveData()

    val productPriceTitle: MutableLiveData<String> = MutableLiveData()


    lateinit var suid: String


    private var subscription: Disposable? = null

    init {

    }


    fun loadProduct(suid: String) {

        this.suid = suid


    }

    fun checkOut(message:String){

    }



    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }




}