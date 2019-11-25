package com.ronaker.app.ui.orderPreview


import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderPreviewViewModel (app: Application): BaseViewModel(app) {

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
    val acceptIntro: MutableLiveData<Boolean> = MutableLiveData()
    val declineIntro: MutableLiveData<Boolean> = MutableLiveData()
    val startRenting: MutableLiveData<Boolean> = MutableLiveData()
    val finishIntro: MutableLiveData<Boolean> = MutableLiveData()
    val cancelDialog: MutableLiveData<Boolean> = MutableLiveData()

    val finish: MutableLiveData<Boolean> = MutableLiveData()

    val showProduct: MutableLiveData<Product> = MutableLiveData()

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
    val orderIntroduction: MutableLiveData<String> = MutableLiveData()



    val orderDescriptionVisibility: MutableLiveData<Int> = MutableLiveData()
    val orderIntroductionVisibility: MutableLiveData<Int> = MutableLiveData()


    val actionVisibility: MutableLiveData<Int> = MutableLiveData()
    val userInfoVisibility: MutableLiveData<Int> = MutableLiveData()


    val acceptVisibility: MutableLiveData<Int> = MutableLiveData()
    val declineVisibility: MutableLiveData<Int> = MutableLiveData()
    val finishVisibility: MutableLiveData<Int> = MutableLiveData()
    val cancelVisibility: MutableLiveData<Int> = MutableLiveData()

    val startRentingVisibility: MutableLiveData<Int> = MutableLiveData()

    lateinit var mOrder: Order


    private var subscription: Disposable? = null

    private var acceptSubscription: Disposable? = null

    private var declineSubscription: Disposable? = null
    private var cancelSubscription: Disposable? = null
    private var finishSubscription: Disposable? = null


    init {

    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
        declineSubscription?.dispose()
        cancelSubscription?.dispose()
        finishSubscription?.dispose()
    }


    fun load(order: Order) {
        mOrder = order


        productTitle.value = order.product.name

        productImage.value = BASE_URL + order.product.avatar

        orderDescription.value = order.message

        startDayName.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(order.fromDate)
        firstDayMonth.value =
            SimpleDateFormat("dd MMMM", Locale.getDefault()).format(order.fromDate)


        endDayName.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(order.toDate)
        endDayMonth.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(order.toDate)


        val days = TimeUnit.DAYS.convert(
            order.toDate.time - order.fromDate.time,
            TimeUnit.MILLISECONDS
        )

        dayNumber.value = String.format(
            "%s%.02f for %d days",
            context.getString(R.string.title_curency_symbol),
            (order.product.price_per_day ?: 0.toDouble()) * days,
            days
        )


        when (Order.OrderTypeEnum[order.orderType]) {

            Order.OrderTypeEnum.Lending -> {

                order.productOwner?.let {

                    userName.value = it.first_name + " " + it.last_name
                    it.avatar?.let {image-> userImage.value= BASE_URL+image }
                    userInfoVisibility.value = View.VISIBLE
                } ?: run {
                    userInfoVisibility.value = View.GONE

                }



                orderDescriptionVisibility.value=View.GONE
                orderIntroductionVisibility.value=View.VISIBLE


                when (Order.OrderStatusEnum[order.status]) {






                    Order.OrderStatusEnum.Accepted -> {

                        actionVisibility.value = View.VISIBLE

                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.GONE

                        startRentingVisibility.value = View.VISIBLE

                        cancelVisibility.value = View.VISIBLE

                    }
                    Order.OrderStatusEnum.Started -> {

                        actionVisibility.value = View.GONE

                    }
                    Order.OrderStatusEnum.Canceled -> {

                        actionVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Finished -> {

                        actionVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Rejected -> {

                        actionVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Pending -> {


                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.GONE
                        cancelVisibility.value = View.VISIBLE
                    }
                    Order.OrderStatusEnum.None -> {

                        actionVisibility.value = View.GONE
                    }
                }

            }
            Order.OrderTypeEnum.Renting -> {
                order.orderUser?.let {

                    userName.value = it.first_name + " " + it.last_name
                    it.avatar?.let { image-> userImage.value= BASE_URL+image }

                    userInfoVisibility.value = View.VISIBLE
                } ?: run {
                    userInfoVisibility.value = View.GONE

                }


                orderDescriptionVisibility.value=View.VISIBLE
                orderIntroductionVisibility.value=View.GONE

                when (Order.OrderStatusEnum[order.status]) {
                    Order.OrderStatusEnum.Accepted -> {

                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.VISIBLE
                        cancelVisibility.value = View.VISIBLE
                    }
                    Order.OrderStatusEnum.Started -> {

                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.VISIBLE
                        cancelVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Canceled -> {

                        actionVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Finished -> {

                        actionVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Rejected -> {

                        actionVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Pending -> {

                        actionVisibility.value = View.VISIBLE

                        acceptVisibility.value = View.VISIBLE
                        declineVisibility.value = View.VISIBLE
                        finishVisibility.value = View.GONE
                        startRentingVisibility.value = View.GONE
                        cancelVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.None -> {

                        actionVisibility.value = View.GONE
                    }
                }

            }
            else -> {
                actionVisibility.value = View.GONE
            }


        }


    }


    fun onClickAccept() {


        acceptIntro.value = true
//        acceptSubscription?.dispose()
//        acceptSubscription = orderRepository.updateOrderStatus(
//            userRepository.getUserToken(),
//            mOrder.suid,
//            "accepted"
//        )
//            .doOnSubscribe { loading.value = true }
//            .doOnTerminate { loading.value = false }
//            .subscribe { result ->
//                if (result.isSuccess() || result.isAcceptable()) {
//                    finish.value=true
//
//                } else {
//
//                    errorMessage.value = result.error?.detail
//                }
//            }


    }


    fun onClickDecline() {


        declineIntro.value = true
//        declineSubscription?.dispose()
//        declineSubscription = orderRepository.updateOrderStatus(
//            userRepository.getUserToken(),
//            mOrder.suid,
//            "rejected"
//        )
//            .doOnSubscribe { loading.value = true }
//            .doOnTerminate { loading.value = false }
//            .subscribe { result ->
//                if (result.isSuccess() || result.isAcceptable()) {
//                    finish.value=true
//
//                } else {
//
//                    errorMessage.value = result.error?.detail
//                }
//            }

    }


    fun onClickItem() {
        showProduct.value = mOrder.product
    }

    fun onClickCanceled() {

        cancelDialog.value=true


    }
    fun canceled(){
        cancelSubscription?.dispose()
        cancelSubscription = orderRepository.updateOrderStatus(
            userRepository.getUserToken(),
            mOrder.suid,
            "canceled"
        )
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess() || result.isAcceptable()) {
                    finish.value = true

                } else {

                    errorMessage.value = result.error?.detail
                }
            }
    }


    fun onClickStartRenting() {

        startRenting.value = true


    }

    fun onClickFinished() {

        finishIntro.value=true
//        finishSubscription?.dispose()
//        finishSubscription = orderRepository.updateOrderStatus(
//            userRepository.getUserToken(),
//            mOrder.suid,
//            "finished"
//        )
//            .doOnSubscribe { loading.value = true }
//            .doOnTerminate { loading.value = false }
//            .subscribe { result ->
//                if (result.isSuccess() || result.isAcceptable()) {
//                    finish.value = true
//
//
//                } else {
//
//                    errorMessage.value = result.error?.detail
//                }
//            }

    }


}