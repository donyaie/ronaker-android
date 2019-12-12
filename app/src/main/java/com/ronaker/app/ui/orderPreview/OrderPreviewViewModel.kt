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
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.toCurrencyFormat
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderPreviewViewModel(val app: Application) : BaseViewModel(app) {

    @Inject
    lateinit
    var orderRepository: OrderRepository


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val recieptVisibility: MutableLiveData<Int> = MutableLiveData()

    var dataList: ArrayList<Order.OrderPrices> = ArrayList()

    var priceListAdapter: OrderPreviewPriceAdapter

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val acceptIntro: MutableLiveData<Boolean> = MutableLiveData()
    val declineIntro: MutableLiveData<Boolean> = MutableLiveData()
    val startRenting: MutableLiveData<Boolean> = MutableLiveData()
    val startRate: MutableLiveData<Boolean> = MutableLiveData()

    val orderStatusImage: MutableLiveData<Int> = MutableLiveData()
    val orderStatus: MutableLiveData<String> = MutableLiveData()


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


    val orderCancelRes: MutableLiveData<String> = MutableLiveData()
    val orderAddress: MutableLiveData<String> = MutableLiveData()


    val orderDescriptionVisibility: MutableLiveData<Int> = MutableLiveData()
    val orderIntroductionVisibility: MutableLiveData<Int> = MutableLiveData()


    val actionVisibility: MutableLiveData<Int> = MutableLiveData()
    val userInfoVisibility: MutableLiveData<Int> = MutableLiveData()


    val acceptVisibility: MutableLiveData<Int> = MutableLiveData()
    val declineVisibility: MutableLiveData<Int> = MutableLiveData()
    val finishVisibility: MutableLiveData<Int> = MutableLiveData()
    val cancelVisibility: MutableLiveData<Int> = MutableLiveData()
    val startRatingVisibility: MutableLiveData<Int> = MutableLiveData()

    val orderCancelResVisibility: MutableLiveData<Int> = MutableLiveData()
    val orderAddressVisibility: MutableLiveData<Int> = MutableLiveData()

    val userContactVisibility: MutableLiveData<Int> = MutableLiveData()


    val startRentingVisibility: MutableLiveData<Int> = MutableLiveData()

    private lateinit var mOrder: Order


    private var subscription: Disposable? = null

    private var acceptSubscription: Disposable? = null

    private var declineSubscription: Disposable? = null
    private var cancelSubscription: Disposable? = null
    private var finishSubscription: Disposable? = null


    init {

        priceListAdapter = OrderPreviewPriceAdapter(dataList)
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
        declineSubscription?.dispose()
        cancelSubscription?.dispose()
        finishSubscription?.dispose()
    }


    fun onClickMakeCall() {

        when (Order.OrderTypeEnum[mOrder.orderType]) {

            Order.OrderTypeEnum.Lending -> {

                mOrder.productOwner?.let {

                    it.phone_number?.let { value -> IntentManeger.makeCall(app, value) }
                }
            }


            Order.OrderTypeEnum.Renting -> {

                mOrder.orderUser?.let {

                    it.phone_number?.let { value -> IntentManeger.makeCall(app, value) }
                }
            }
            else -> {
            }
        }
    }

    fun onClickSendMail() {

        when (Order.OrderTypeEnum[mOrder.orderType]) {

            Order.OrderTypeEnum.Lending -> {

                mOrder.productOwner?.let {

                    it.email?.let { value -> IntentManeger.sendMail(app, value) }
                }
            }


            Order.OrderTypeEnum.Renting -> {

                mOrder.orderUser?.let {

                    it.email?.let { value -> IntentManeger.sendMail(app, value) }
                }
            }
            else -> {
            }
        }
    }


    fun load(order: Order) {
        mOrder = order


        productTitle.value = order.product.name

        productImage.value = BASE_URL + order.product.avatar


        startDayName.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(order.fromDate)
        firstDayMonth.value =
            SimpleDateFormat("dd MMMM", Locale.getDefault()).format(order.fromDate)


        endDayName.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(order.toDate)
        endDayMonth.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(order.toDate)


        orderAddress.value = order.address
        orderIntroduction.value = order.instruction
        orderCancelRes.value = order.rejectionReason
        orderDescription.value = order.message


        orderAddressVisibility.value =
            if (order.address.isNullOrBlank()) View.GONE else View.VISIBLE
        orderIntroductionVisibility.value =
            if (order.instruction.isNullOrBlank()) View.GONE else View.VISIBLE
        orderCancelResVisibility.value =
            if (order.rejectionReason.isNullOrBlank()) View.GONE else View.VISIBLE
        orderDescriptionVisibility.value =
            if (order.message.isNullOrBlank()) View.GONE else View.VISIBLE








        order.price?.let {

            dataList.clear()
            dataList.addAll(it)
            priceListAdapter.notifyDataSetChanged()
        }


        val days = TimeUnit.DAYS.convert(
            order.toDate.time - order.fromDate.time,
            TimeUnit.MILLISECONDS
        )




        when (Order.OrderTypeEnum[order.orderType]) {

            Order.OrderTypeEnum.Lending -> {

                order.productOwner?.let {

                    userName.value = it.first_name + " " + it.last_name
                    it.avatar?.let { image -> userImage.value = BASE_URL + image }
                    userInfoVisibility.value = View.VISIBLE
                } ?: run {
                    userInfoVisibility.value = View.GONE

                }

                recieptVisibility.value = View.VISIBLE

                startRatingVisibility.value = View.GONE




                var total = 0.0
                order.price?.forEach {
                    if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.Total)
                        total = if (it.price == 0.0) 0.0 else it.price / 100.0
                }


                dayNumber.value = String.format(
                    "%s %s for %d days",
                    context.getString(R.string.text_you_pay),
                    total.toCurrencyFormat(),
                    days
                )






                when (Order.OrderStatusEnum[order.status]) {

                    Order.OrderStatusEnum.Accepted -> {

                        actionVisibility.value = View.VISIBLE

                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.GONE
                        startRentingVisibility.value = View.VISIBLE
                        cancelVisibility.value = View.VISIBLE

                        userContactVisibility.value = View.VISIBLE

                    }
                    Order.OrderStatusEnum.Started -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.VISIBLE

                    }
                    Order.OrderStatusEnum.Canceled -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.VISIBLE
                    }
                    Order.OrderStatusEnum.Finished -> {

                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.GONE
                        cancelVisibility.value = View.GONE

                        startRatingVisibility.value = View.VISIBLE

                        userContactVisibility.value = View.GONE

                    }
                    Order.OrderStatusEnum.Rejected -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Pending -> {


                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.GONE
                        cancelVisibility.value = View.VISIBLE

                        userContactVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.None -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.GONE
                    }
                }

            }
            Order.OrderTypeEnum.Renting -> {
                order.orderUser?.let {

                    userName.value = it.first_name + " " + it.last_name
                    it.avatar?.let { image -> userImage.value = BASE_URL + image }

                    userInfoVisibility.value = View.VISIBLE
                } ?: run {
                    userInfoVisibility.value = View.GONE

                }

                startRatingVisibility.value = View.GONE

                var total = 0.0
                order.price?.forEach {
                    if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.ProductFee)
                        total = if (it.price == 0.0) 0.0 else it.price / 100.0
                }


                dayNumber.value = String.format(
                    "%s %s for %d days",
                    context.getString(R.string.text_you_earn),
                    total.toCurrencyFormat(),
                    days
                )


                recieptVisibility.value = View.GONE
                when (Order.OrderStatusEnum[order.status]) {
                    Order.OrderStatusEnum.Accepted -> {

                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.GONE
                        cancelVisibility.value = View.VISIBLE

                        userContactVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Started -> {

                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.GONE
                        acceptVisibility.value = View.GONE
                        declineVisibility.value = View.GONE
                        finishVisibility.value = View.VISIBLE
                        cancelVisibility.value = View.GONE

                        userContactVisibility.value = View.VISIBLE
                    }
                    Order.OrderStatusEnum.Canceled -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Finished -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Rejected -> {

                        actionVisibility.value = View.GONE

                        userContactVisibility.value = View.GONE
                    }
                    Order.OrderStatusEnum.Pending -> {

                        actionVisibility.value = View.VISIBLE

                        acceptVisibility.value = View.VISIBLE
                        declineVisibility.value = View.VISIBLE
                        finishVisibility.value = View.GONE
                        startRentingVisibility.value = View.GONE
                        cancelVisibility.value = View.GONE

                        userContactVisibility.value = View.GONE
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


        val ownerName =
            (order.productOwner?.first_name ?: "") + " " + (order.productOwner?.last_name ?: "")

        val orderedUserName =
            (order.orderUser?.first_name ?: "") + " " + (order.orderUser?.last_name ?: "")



        when (Order.OrderStatusEnum[order.status]) {
            Order.OrderStatusEnum.Accepted -> {

                orderStatusImage.value = R.drawable.ic_guide_success

                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.value =
                        app.getString(R.string.text_rent_request_accepted, orderedUserName)
                } else {

                    orderStatus.value =
                        app.getString(R.string.text_lend_request_accepted, ownerName)
                }


            }
            Order.OrderStatusEnum.Started -> {

                orderStatusImage.value = R.drawable.ic_guide_success

                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.value =
                        app.getString(R.string.text_rent_request_started, orderedUserName)
                } else {

                    orderStatus.value = app.getString(R.string.text_lend_request_started, ownerName)
                }


            }
            Order.OrderStatusEnum.Canceled -> {

                orderStatusImage.value = R.drawable.ic_remove_red

                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = app.getString(R.string.text_rent_canceled)
                } else {

                    orderStatus.value = app.getString(R.string.text_lend_canceled)
                }


            }
            Order.OrderStatusEnum.Finished -> {

                orderStatusImage.value = R.drawable.ic_guide_success


                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = app.getString(R.string.text_rent_complete)
                } else {

                    orderStatus.value = app.getString(R.string.text_lend_complete)
                }
            }
            Order.OrderStatusEnum.Pending -> {

                orderStatusImage.value = R.drawable.ic_pending


                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = app.getString(R.string.text_rent_request_pending)
                } else {
                    orderStatus.value = app.getString(R.string.text_lend_request_pending, ownerName)
                }

            }

            Order.OrderStatusEnum.Rejected -> {

                orderStatusImage.value = R.drawable.ic_remove_red


                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = app.getString(R.string.text_rent_rejected)
                } else {

                    orderStatus.value = app.getString(R.string.text_lend_rejected)
                }

            }
            else -> {

                orderStatusImage.value = R.drawable.ic_remove_red

                orderStatus.value = "Not Set"
            }


        }


    }


    fun onClickAccept() {


        acceptIntro.value = true


    }


    fun onClickDecline() {


        declineIntro.value = true


    }


    fun onClickItem() {
        showProduct.value = mOrder.product
    }

    fun onClickCanceled() {

        cancelDialog.value = true


    }

    fun canceled() {
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

                    errorMessage.value = result.error?.message
                }
            }
    }


    fun onClickStartRenting() {

        startRenting.value = true


    }

    fun onClickStartRate() {

        startRate.value = true
    }

    fun onClickFinished() {

        finishIntro.value = true


    }


}