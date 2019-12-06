package com.ronaker.app.ui.orderMessage


import android.app.Application
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

class OrderMessageViewModel (app: Application): BaseViewModel(app) {

    @Inject
    lateinit
    var orderRepository: OrderRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val next: MutableLiveData<Boolean> = MutableLiveData()
    val successMessage: MutableLiveData<Boolean> = MutableLiveData()


    val productPrice: MutableLiveData<String> = MutableLiveData()
    val orderMessage: MutableLiveData<String> = MutableLiveData()

    val productPriceTitle: MutableLiveData<String> = MutableLiveData()
    @Inject
    lateinit
    var context: Context

    private lateinit var mProduct: Product
    private lateinit var mStartDate: Date
    private lateinit var mEndDate: Date

    private var mPrice = 0.0


    private var subscription: Disposable? = null



    fun loadProduct(product: Product, startDate: Date, endDAte: Date) {

        this.mProduct = product

        this.mStartDate = startDate
        this.mEndDate = endDAte

        val days = TimeUnit.DAYS.convert(
            endDAte.time -
                    startDate.time,
            TimeUnit.MILLISECONDS
        )

        mPrice = (product.price_per_day ?: 0.toDouble()) * days

        productPriceTitle.value = "for $days days"
        productPrice.value =
            String.format("%s%.02f", context.getString(R.string.title_curency_symbol), mPrice)


        val user = userRepository.getUserInfo()


//        orderMessage.value = "Hi I'm ${user?.first_name} ${user?.last_name}\n" +
//                "I to rent your ${product.name} for $days day${if (days == 1L) "" else "s"}\n" +
//                "thank you."
        orderMessage.value = context.getString(
            R.string.text_order_message,
            user?.first_name,
            user?.last_name,
            product.name,
            days.toString()
        )


    }

    fun checkOut(message: String) {
        subscription?.dispose()
        mProduct.suid?.let {
            subscription =
                orderRepository.createOrder(
                    userRepository.getUserToken(),
                    it,
                    mStartDate,
                    mEndDate,
                    message,
                    mPrice*100
                )
                    .doOnSubscribe { loading.value = true }
                    .doOnTerminate { loading.value = false }
                    .subscribe { result ->
                        if (result.isSuccess() || result.isAcceptable()) {
                            successMessage.value = true


                        } else {

                            errorMessage.value = result.error?.message
                        }
                    }


        }


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}