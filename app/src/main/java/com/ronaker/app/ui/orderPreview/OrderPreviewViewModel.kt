package com.ronaker.app.ui.orderPreview


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderPreviewViewModel : BaseViewModel() {

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



    val productTitle: MutableLiveData<String> = MutableLiveData()
    val dayNumber: MutableLiveData<String> = MutableLiveData()
    val productImage: MutableLiveData<String> = MutableLiveData()
    val startDayName: MutableLiveData<String> = MutableLiveData()
    val firstDayMonth: MutableLiveData<String> = MutableLiveData()
    val endDayName: MutableLiveData<String> = MutableLiveData()
    val endDayMonth: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val userImage: MutableLiveData<String> = MutableLiveData()
    val orderDescription: MutableLiveData<String> = MutableLiveData()


    val acceptVisibility: MutableLiveData<Int> = MutableLiveData()
    val declineVisibility: MutableLiveData<Int> = MutableLiveData()
    val actionVisibility: MutableLiveData<Int> = MutableLiveData()


    val finishVisibility: MutableLiveData<Int> = MutableLiveData()
    val cancelVisibility: MutableLiveData<Int> = MutableLiveData()

    lateinit var mOrder: Order


    private var subscription: Disposable? = null

    private var acceptSubscription: Disposable? = null

    private var declineSubscription: Disposable? = null


    init {

    }





    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
        declineSubscription?.dispose()
    }

    fun load(order: Order) {
        mOrder=order


        productTitle.value=order.product.name

        productImage.value= BASE_URL+ order.product.avatar

        orderDescription.value=order.message

        startDayName.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(order.fromDate)
        firstDayMonth.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(order.fromDate)


        endDayName.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(order.toDate)
        endDayMonth.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(order.toDate)



        val days = TimeUnit.DAYS.convert(
            order.toDate.time  - order.fromDate.time ,
            TimeUnit.MILLISECONDS
        )

        dayNumber.value = String.format("%s%.02f for %d days", context.getString(R.string.title_curency_symbol), order.product.price_per_day!! * days,days)


        when(Order.OrderTypeEnum.get( order.orderType)){

            Order.OrderTypeEnum.Lending->{

                userName.value= order.productOwner?.first_name +" "+order.productOwner?.last_name
            }
            Order.OrderTypeEnum.Renting->{


                userName.value= order.orderUser?.first_name +" "+order.orderUser?.last_name


            }


        }


    }


    fun onClickAccept(){

        acceptSubscription=orderRepository.updateOrderStatus(userRepository.getUserToken(),mOrder.suid,"accepted")
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()||result.isAcceptable()) {
                    errorMessage.value = "accepted"

                } else {

                    errorMessage.value = result.error?.detail
                }
            }



    }



    fun onClickDecline(){
        acceptSubscription=orderRepository.updateOrderStatus(userRepository.getUserToken(),mOrder.suid,"rejected")
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()||result.isAcceptable()) {
                    errorMessage.value = "rejected"

                } else {

                    errorMessage.value = result.error?.detail
                }
            }

    }


    fun onClickCanceled(){
        acceptSubscription=orderRepository.updateOrderStatus(userRepository.getUserToken(),mOrder.suid,"canceled")
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()||result.isAcceptable()) {
                    errorMessage.value = "canceled"

                } else {

                    errorMessage.value = result.error?.detail
                }
            }

    }


    fun onClickFinished(){
        acceptSubscription=orderRepository.updateOrderStatus(userRepository.getUserToken(),mOrder.suid,"finished")
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()||result.isAcceptable()) {
                    errorMessage.value = "finished"

                } else {

                    errorMessage.value = result.error?.detail
                }
            }

    }




}