package com.ronaker.app.ui.orderStartRenting


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.ui.orderPreview.OrderPreviewPriceAdapter
import com.ronaker.app.ui.profilePaymentList.PaymentSelectAdapter
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.TERMS_URL
import io.reactivex.disposables.Disposable
import java.util.ArrayList
import javax.inject.Inject

class OrderStartRentingViewModel (val app: Application): BaseViewModel(app) {

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


   fun onClickTerms(){
       IntentManeger.openUrl(context,TERMS_URL)
   }

    fun load(order: Order) {
        mOrder = order

        order.price?.let {

            dataList.clear()
            dataList.addAll(it)
            priceListAdapter.notifyDataSetChanged()
        }



        cardDataList.clear()
        cardDataList.add(PaymentCard("9873946297430","***** 0836",PaymentCard.CardType.MASTERCARD.key).apply { selected=true })
        cardDataList.add(PaymentCard("987394629987430","***** 0985",PaymentCard.CardType.VISA.key))
        cardDataList.add(PaymentCard("987394674297430","***** 9009",PaymentCard.CardType.DINERS_CLUB.key))
        cardListAdapter.notifyDataSetChanged()


    }


    fun onClickAccept() {
        acceptSubscription?.dispose()
        acceptSubscription = orderRepository.updateOrderStatus(
            userRepository.getUserToken(),
            mOrder.suid,
            "started"
        )
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess() || result.isAcceptable()) {
                    finish.value=true

                } else {
                    finish.value=true
                    errorMessage.value = "Successfully Send"
                }
            }


    }




}