package com.ronaker.app.ui.orderStartRenting


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.ui.orderPreview.OrderPreviewPriceAdapter
import com.ronaker.app.ui.profilePaymentList.PaymentSelectAdapter
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.TERM_URL
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class OrderStartRentingViewModel(val app: Application) : BaseViewModel(app) {

    @Inject
    lateinit
    var orderRepository: OrderRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    @Inject
    lateinit
    var paymentInfoRepository: PaymentInfoRepository


    @Inject
    lateinit
    var context: Context

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val instruction: MutableLiveData<String> = MutableLiveData()
    val orderAddress: MutableLiveData<String> = MutableLiveData()


    var dataList: ArrayList<Order.OrderPrices> = ArrayList()

    var priceListAdapter: OrderPreviewPriceAdapter


    var cardDataList: ArrayList<PaymentCard> = ArrayList()

    var cardListAdapter: PaymentSelectAdapter

    val finish: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mOrder: Order


    private var subscription: Disposable? = null

    private var acceptSubscription: Disposable? = null


    init {

        priceListAdapter = OrderPreviewPriceAdapter(dataList)
        cardListAdapter = PaymentSelectAdapter(cardDataList)
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
    }


    fun onClickTerms() {
        IntentManeger.openUrl(context, TERM_URL)
    }

    fun load(order: Order) {
        mOrder = order

        order.price?.let {

            dataList.clear()
            dataList.addAll(it)
            dataList.removeAll {  price ->Order.OrderPriceEnum[price.key] ==Order.OrderPriceEnum.InsuranceFee }
            priceListAdapter.notifyDataSetChanged()
        }

        cardDataList.clear()

        cardDataList.add(
            PaymentCard(
                paymentInfoType = PaymentCard.PaymentType.Cash.key,
                fullName = "By Cash",
                isVerified = true
            ).apply { selected = true })
        cardListAdapter.notifyDataSetChanged()
        subscription?.dispose()
        subscription = paymentInfoRepository.getPaymentInfoList(
            userRepository.getUserToken()
        )
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.let {
                        if (it.isNotEmpty()) {
                            cardDataList.addAll(it)
                            cardListAdapter.notifyDataSetChanged()
                        }

                    }

                } else {
                    finish.value = true
                    errorMessage.value = "Successfully Send"
                }
            }


    }


    fun onClickAccept() {
        acceptSubscription?.dispose()
        acceptSubscription = orderRepository.updateOrderStatus(
            token = userRepository.getUserToken(),
            suid = mOrder.suid,
            status = "started"
        )
            .doOnSubscribe { loadingButton.postValue(true)  }
            .doOnTerminate { loadingButton.postValue(false) }
            .subscribe { result ->
                if (result.isSuccess() || result.isAcceptable()) {
                    finish.postValue(true)

                } else {
                    errorMessage.postValue(result.error?.message)
                }
            }


    }


}