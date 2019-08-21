package com.ronaker.app.ui.orderMessage


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class OrderMessageViewModel : BaseViewModel() {

    @Inject
    lateinit
    var orderRepository: OrderRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val next: MutableLiveData<Boolean> = MutableLiveData()


    val productPrice: MutableLiveData<String> = MutableLiveData()

    val productPriceTitle: MutableLiveData<String> = MutableLiveData()


    lateinit var mProduct: Product
    lateinit var mStartDate: Date
    lateinit var mEndDate: Date


    private var subscription: Disposable? = null

    init {

    }


    fun loadProduct(product: Product, startDate: Date, endDAte:Date) {

        this.mProduct = product

        this.mStartDate=startDate
        this.mEndDate=endDAte








    }

    fun checkOut(message:String){


        subscription=orderRepository.createOrder(userRepository.getUserToken(),mProduct.suid!!,mStartDate,mEndDate,message)
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()|| result.isAcceptable()) {

                    next.value=true

                } else {

                    errorMessage.value = result.error?.detail
                }
            }




    }



    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }




}