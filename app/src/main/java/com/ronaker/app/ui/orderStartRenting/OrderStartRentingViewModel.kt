package com.ronaker.app.ui.orderStartRenting


import android.view.View
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
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OrderStartRentingViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val paymentInfoRepository: PaymentInfoRepository,
    private val resourcesRepository: ResourcesRepository
) : BaseViewModel() {


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()
    val doSignContract: MutableLiveData<Boolean> = MutableLiveData()
    val startRentingConfirm: MutableLiveData<String> = MutableLiveData()


    val contractPreview: MutableLiveData<Boolean> = MutableLiveData()

    val instruction: MutableLiveData<String> = MutableLiveData()


    val contractPreviewVisibility: MutableLiveData<Int> = MutableLiveData()
    val listerSignImage: MutableLiveData<Int> = MutableLiveData()
    val listerSignText: MutableLiveData<String> = MutableLiveData()
    val paymentInit: MutableLiveData<String> = MutableLiveData()
    val renterSignImage: MutableLiveData<Int> = MutableLiveData()
    val renterSignText: MutableLiveData<String> = MutableLiveData()
    val contractSignVisibility: MutableLiveData<Int> = MutableLiveData()
    val contractSignMessageVisibility: MutableLiveData<Int> = MutableLiveData()


    var dataList: ArrayList<Order.OrderPrices> = ArrayList()

    var priceListAdapter: OrderPreviewPriceAdapter = OrderPreviewPriceAdapter(dataList)


    var cardDataList: ArrayList<PaymentCard> = ArrayList()

    var cardListAdapter: PaymentSelectAdapter = PaymentSelectAdapter(cardDataList)

    val finish: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mOrder: Order


    private var subscription: Disposable? = null
    private var initPaysubscription: Disposable? = null
    private var checkPaysubscription: Disposable? = null
    private var orderSubscription: Disposable? = null

    private var acceptSubscription: Disposable? = null


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        acceptSubscription?.dispose()
        orderSubscription?.dispose()
        initPaysubscription?.dispose()
        checkPaysubscription?.dispose();
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
//            dataList.removeAll { price -> Order.OrderPriceEnum[price.key] == Order.OrderPriceEnum.InsuranceFee }
            priceListAdapter.notifyDataSetChanged()
        }




        if (order.smart_id_owner_session_id.isNullOrBlank()) {

            listerSignText.postValue(
                String.format(
                    resourcesRepository.getString(R.string.text_waite_for_sign),
                    nameFormat(order.productOwner?.first_name, order.productOwner?.last_name)
                )
            )
            listerSignImage.postValue(R.drawable.ic_guide_red)
        } else {

            listerSignText.postValue(
                String.format(
                    resourcesRepository.getString(R.string.text_signed_the_contract),
                    nameFormat(order.productOwner?.first_name, order.productOwner?.last_name)
                )
            )
            listerSignImage.postValue(R.drawable.ic_guide_success)
        }



        if (order.smart_id_creator_session_id.isNullOrBlank()) {


            contractSignVisibility.postValue(View.VISIBLE)
            contractSignMessageVisibility.postValue(View.GONE)
            renterSignText.postValue("")
            renterSignImage.postValue(R.drawable.ic_guide_red)


        } else {
            contractSignVisibility.postValue(View.GONE)

            renterSignText.postValue(resourcesRepository.getString(R.string.text_you_signed_the_contract))

            renterSignImage.postValue(R.drawable.ic_guide_success)

            contractSignMessageVisibility.postValue(View.VISIBLE)

        }




        contractPreviewVisibility.postValue(View.VISIBLE)


        cardDataList.clear()
//
//        cardDataList.add(
//            PaymentCard(
//                paymentInfoType = PaymentCard.PaymentType.Cash.key,
//                fullName = "By Cash",
//                isVerified = true
//            ).apply { selected = true })
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
                            if (cardDataList.isNotEmpty()) {
                                cardDataList[0].selected = true
                            }
                            cardListAdapter.notifyDataSetChanged()
                        }

                    }

                } else {
                    finish.value = false
                    errorMessage.value = "Successfully Send"
                }
            }


    }

    var paymentSecret: String? = null
    var isPaymentChecked: Boolean = false

    fun checkedAgreement() {
        mOrder.address?.let { startRentingConfirm.postValue(it) }
    }

    //{"payment_id":["This field is required."]}
    fun chechPayment(paymentId: String) {
        checkPaysubscription?.dispose()
        checkPaysubscription = orderRepository.recheckPaymentAuth(mOrder.suid, paymentId)
            .doOnSubscribe { loadingButton.value = true }
            .doOnTerminate { loadingButton.value = false }
            .subscribe { result ->
                if (result.isAcceptable()) {
                    isPaymentChecked = true;
                    startRenting()
//                    errorMessage.value = "isAcceptable"
                } else {
                    errorMessage.value = result?.error?.message
                }
            }
    }

    fun onClickAccept() {

//        if(!isPaymentChecked){
//            checkPaysubscription?.dispose()
//            checkPaysubscription= orderRepository.recheckPaymentAuth(mOrder.suid)
//                .doOnSubscribe { loadingButton.value = true }
//                .doOnTerminate { loadingButton.value = false }
//                .subscribe { result ->
//                    if (result.isSuccess()) {
//                        isPaymentChecked=true;
//                        onClickAccept();
//
//                    } else {
//                        errorMessage.value = result?.error?.message
//                    }
//                }
//        }else

//        if (paymentSecret == null) {
            initPaysubscription?.dispose()
            initPaysubscription = orderRepository.initialPayment(mOrder.suid)
                .doOnSubscribe { loadingButton.value = true }
                .doOnTerminate { loadingButton.value = false }
                .subscribe { result ->
                    if (result.isSuccess()) {

                        paymentSecret = result?.data?.client_secret


                        paymentInit.postValue(paymentSecret)

                    } else {
                        errorMessage.value = result?.error?.message
                    }
                }
//        } else {
//
//            paymentInit.postValue(paymentSecret)
//
//        }


//        if(!mOrder.smart_id_creator_session_id.isNullOrBlank() && !mOrder.smart_id_owner_session_id.isNullOrBlank()){
//            startRenting()
//        }else{
//            errorMessage.postValue(resourcesRepository.getString(R.string.text_make_sure_sign_the_contract))
//            loadData(mOrder.suid)
//        }


//        startRenting()


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

    fun onClickSign() {

        doSignContract.postValue(true)
    }


}