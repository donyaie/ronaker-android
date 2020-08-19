package com.ronaker.app.ui.orderStartRenting


import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.model.Order
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.ui.orderPreview.OrderPreviewPriceAdapter
import com.ronaker.app.ui.profilePaymentList.PaymentSelectAdapter
import com.ronaker.app.utils.nameFormat
import io.reactivex.disposables.Disposable
import java.util.*

class OrderStartRentingViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository,
    private val  paymentInfoRepository: PaymentInfoRepository,
    private val  resourcesRepository: ResourcesRepository
)  : BaseViewModel() {


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()
    val doSignContract: MutableLiveData<Boolean> = MutableLiveData()
    val contractPreview: MutableLiveData<Boolean> = MutableLiveData()

    val instruction: MutableLiveData<String> = MutableLiveData()


    val contractPreviewVisibility: MutableLiveData<Int> = MutableLiveData()
    val lenderSignImage: MutableLiveData<Int> = MutableLiveData()
    val listerSignText: MutableLiveData<String> = MutableLiveData()




    var dataList: ArrayList<Order.OrderPrices> = ArrayList()

    var priceListAdapter: OrderPreviewPriceAdapter


    var cardDataList: ArrayList<PaymentCard> = ArrayList()

    var cardListAdapter: PaymentSelectAdapter

    val finish: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mOrder: Order


    private var subscription: Disposable? = null
    private var orderSubscription: Disposable? = null

    private var acceptSubscription: Disposable? = null


    init {

        priceListAdapter = OrderPreviewPriceAdapter(dataList)
        cardListAdapter = PaymentSelectAdapter(cardDataList)
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
        orderSubscription?.dispose()
    }




    fun loadData(suid: String) {

        orderSubscription?.dispose()
        orderSubscription = orderRepository.getOrderDetail(
            suid = suid
        )
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.let { loadData(it) }

                } else {

                    errorMessage.postValue(result.error?.message)
                }
            }


    }


    fun loadData(order: Order) {
        mOrder = order

        order.price?.let {

            dataList.clear()
            dataList.addAll(it)
            dataList.removeAll { price -> Order.OrderPriceEnum[price.key] == Order.OrderPriceEnum.InsuranceFee }
            priceListAdapter.notifyDataSetChanged()
        }




        if (order.smart_id_owner_session_id.isNullOrBlank()) {

            listerSignText.postValue(
                String.format(
                    resourcesRepository.getString(R.string.text_waite_for_sign),
                    nameFormat(order.productOwner?.first_name, order.productOwner?.last_name)
                )
            )
            lenderSignImage.postValue(R.drawable.ic_guide_red)
        } else {

            listerSignText.postValue(
                String.format(
                    resourcesRepository.getString(R.string.text_signed_the_contract),
                    nameFormat(order.productOwner?.first_name, order.productOwner?.last_name)
                )
            )
            lenderSignImage.postValue(R.drawable.ic_guide_success)
        }







        contractPreviewVisibility.postValue(View.VISIBLE)


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

        if (mOrder.smart_id_creator_session_id.isNullOrBlank()) {

            doSignContract.postValue(true)
        }  else if(mOrder.smart_id_owner_session_id.isNullOrBlank()) {
            errorMessage.postValue(resourcesRepository.getString(R.string.text_make_sure_sign_the_contract))
            loadData(mOrder.suid)
        }else{
            startRenting()
        }


    }



    private fun startRenting() {

        acceptSubscription?.dispose()
        acceptSubscription = mOrder.suid.let {
            orderRepository.updateOrderStatus(
                suid = it,
                status = "started"
            )
                .doOnSubscribe { loadingButton.postValue(true) }
                .doOnTerminate { loadingButton.postValue(false) }
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {
                        finish.postValue(true)
                    } else {

                        if (result.error?.responseCode == 406)
                            errorMessage.postValue(resourcesRepository.getString(R.string.text_make_sure_sign_the_contract))
                        else
                            errorMessage.postValue(result.error?.message)


                    }
                }
        }
    }



    fun onContractPreview() {
        contractPreview.postValue(true)
    }



}