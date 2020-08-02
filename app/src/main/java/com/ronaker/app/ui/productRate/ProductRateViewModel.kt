package com.ronaker.app.ui.productRate


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProductRateViewModel(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit
    var orderRepository: OrderRepository

    @Inject
    lateinit
    var userRepository: UserRepository

    @Inject
    lateinit
    var context: Context

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val finish: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mOrder: Order

    private var subscription: Disposable? = null

    private var acceptSubscription: Disposable? = null


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
    }


    fun load(order: Order) {
        mOrder = order
    }


    fun onClickAccept(comment: String, rate: Float) {

        acceptSubscription?.dispose()
        acceptSubscription = orderRepository.orderRate(
            orderSuid = mOrder.suid,
            comment = comment,
            stars = rate.toDouble()

        )
            .doOnSubscribe { loadingButton.value = true }
            .doOnTerminate { loadingButton.value = false }
            .subscribe { result ->
                if (result.isSuccess() || result.isAcceptable()) {
                    finish.value = true

                } else {

                    errorMessage.value = result.error?.message
                }
            }


    }


}