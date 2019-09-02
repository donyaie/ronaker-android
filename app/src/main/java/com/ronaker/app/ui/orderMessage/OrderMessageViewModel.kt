package com.ronaker.app.ui.orderMessage


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
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
    @Inject
    lateinit
    var context: Context

    lateinit var mProduct: Product
    lateinit var mStartDate: Date
    lateinit var mEndDate: Date


    private var subscription: Disposable? = null

    init {

    }


    fun loadProduct(product: Product, startDate: Date, endDAte: Date) {

        this.mProduct = product

        this.mStartDate = startDate
        this.mEndDate = endDAte

        val days = TimeUnit.DAYS.convert(
            endDAte.time -
                    startDate.time,
            TimeUnit.MILLISECONDS
        )

        productPriceTitle.value = "for $days days"
        productPrice.value = String.format("%s%.02f", context.getString(R.string.title_curency_symbol), product.price_per_day!! * days)


    }

    fun checkOut(message: String) {



        subscription =
            orderRepository.createOrder(userRepository.getUserToken(), mProduct.suid!!, mStartDate, mEndDate, message)
                .doOnSubscribe { loading.value = true }
                .doOnTerminate { loading.value = false }
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {

                        next.value = true

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