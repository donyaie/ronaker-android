package com.ronaker.app.ui.orderPreview


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.model.Order
import com.ronaker.app.model.Product
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.nameFormat
import com.ronaker.app.utils.toCurrencyFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class OrderPreviewViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val contentRepository: ContentRepository,
    private val resourcesRepository: ResourcesRepository
)   : BaseViewModel() {



    private val recieptVisibility: MutableLiveData<Int> = MutableLiveData()

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


    val signContractShow: MutableLiveData<Boolean> = MutableLiveData()
    val contractPreview: MutableLiveData<Boolean> = MutableLiveData()

    val previewContractShow: MutableLiveData<File> = MutableLiveData()

    val contractViewENVisibility: MutableLiveData<Int> = MutableLiveData()
    val contractViewLTVisibility: MutableLiveData<Int> = MutableLiveData()
    val contractPreviewVisibility: MutableLiveData<Int> = MutableLiveData()


    val orderCancelRes: MutableLiveData<String> = MutableLiveData()
    val orderAddress: MutableLiveData<String> = MutableLiveData()


    val orderDescriptionVisibility: MutableLiveData<Int> = MutableLiveData()
    val orderIntroductionVisibility: MutableLiveData<Int> = MutableLiveData()


    val actionVisibility: MutableLiveData<Int> = MutableLiveData()
    val userInfoVisibility: MutableLiveData<Int> = MutableLiveData()


    val contractVisibility: MutableLiveData<Int> = MutableLiveData()

    //    val renterSignVisibility: MutableLiveData<Int> = MutableLiveData()
    val listerSignVisibility: MutableLiveData<Int> = MutableLiveData()


    val renterSignImage: MutableLiveData<Int> = MutableLiveData()
    val lenderSignImage: MutableLiveData<Int> = MutableLiveData()

    val renterSignText: MutableLiveData<String> = MutableLiveData()
    val listerSignText: MutableLiveData<String> = MutableLiveData()


    val acceptVisibility: MutableLiveData<Int> = MutableLiveData()
    val declineVisibility: MutableLiveData<Int> = MutableLiveData()
    val finishVisibility: MutableLiveData<Int> = MutableLiveData()
    val cancelVisibility: MutableLiveData<Int> = MutableLiveData()
    val startRatingVisibility: MutableLiveData<Int> = MutableLiveData()

    val orderCancelResVisibility: MutableLiveData<Int> = MutableLiveData()
    val orderAddressVisibility: MutableLiveData<Int> = MutableLiveData()


    val makeCall: MutableLiveData<String> = MutableLiveData()
    val sendEmail: MutableLiveData<String> = MutableLiveData()

    val userContactVisibility: MutableLiveData<Int> = MutableLiveData()


    val startRentingVisibility: MutableLiveData<Int> = MutableLiveData()

    private lateinit var mOrder: Order


    private var subscription: Disposable? = null
    private var fileSubscription: Disposable? = null


    init {

        priceListAdapter = OrderPreviewPriceAdapter(dataList)
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        fileSubscription?.dispose()
    }


    fun onClickMakeCall() {

        when (Order.OrderTypeEnum[mOrder.orderType]) {

            Order.OrderTypeEnum.Lending -> {

                mOrder.productOwner?.let {

                    it.phone_number?.let { value -> makeCall.postValue(value) }


                }
            }


            Order.OrderTypeEnum.Renting -> {

                mOrder.orderUser?.let {

                    it.phone_number?.let { value -> makeCall.postValue(value) }
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

                    it.email?.let { value -> sendEmail.postValue(value) }
                }
            }


            Order.OrderTypeEnum.Renting -> {

                mOrder.orderUser?.let {

                    it.email?.let { value -> sendEmail.postValue(value) }
                }
            }
            else -> {
            }
        }
    }

    fun load(order: Order) {
        fillView(order)

    }

    fun getOrder(): Order {
        return mOrder
    }

    fun getOrder(suid: String) {

        subscription?.dispose()
        subscription = orderRepository
            .getOrderDetail(suid)

            .doOnSubscribe {
//                retry.value = null
//                loading.value = true

                if (!::mOrder.isInitialized)
                    loading.value = true

            }
            .doOnTerminate {
                loading.value = false


            }
            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.let { fillView(it) }
                } else {
                    errorMessage.value = result.error?.message
                }
            }

    }


    private fun getPDF(url: String, name: String) {
        fileSubscription?.dispose()
        fileSubscription = contentRepository
            .downloadFile(
                url,
                "$name-contract.pdf"
            )

            .doOnSubscribe {
            }
            .doOnTerminate {

            }
            .subscribe { result ->
                if (result.isSuccess()) {

                    previewContractShow.postValue(result.data)


                } else {
                    errorMessage.value = result.error?.message
                }
            }
    }


    fun fillView(order: Order) {
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
            if (order.message.isBlank()) View.GONE else View.VISIBLE




        order.price?.let {

            dataList.clear()
            dataList.addAll(it)
            priceListAdapter.notifyDataSetChanged()
        }


        val days = TimeUnit.DAYS.convert(
            order.toDate.time - order.fromDate.time,
            TimeUnit.MILLISECONDS
        )



        order.signPdf_EN?.let {
            contractViewENVisibility.postValue(View.VISIBLE)
        } ?: run {
            contractViewENVisibility.postValue(View.GONE)
        }



        order.signPdf_LT?.let {
            contractViewLTVisibility.postValue(View.VISIBLE)
        } ?: run {
            contractViewLTVisibility.postValue(View.GONE)
        }

        if (order.signPdf_LT.isNullOrEmpty() && order.signPdf_EN.isNullOrEmpty())
            contractPreviewVisibility.postValue(View.VISIBLE)
        else
            contractPreviewVisibility.postValue(View.GONE)


//        contractPreviewVisibility.postValue(View.VISIBLE)


        listerSignVisibility.postValue(View.GONE)

        when (Order.OrderTypeEnum[order.orderType]) {

            Order.OrderTypeEnum.Lending -> {

                order.productOwner?.let {

                    userName.value = nameFormat(it.first_name, it.last_name)
                    it.avatar?.let { image -> userImage.value = BASE_URL + image }
                    userInfoVisibility.value = View.VISIBLE
                } ?: run {
                    userInfoVisibility.value = View.GONE

                }

                recieptVisibility.value = View.VISIBLE


                var total = 0.0
                order.price?.forEach {
                    if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.Total)
                        total = if (it.price == 0.0) 0.0 else it.price / 100.0
                }


                dayNumber.value = String.format(
                    resourcesRepository.getString(R.string.format_you_will_pay),
                    total.toCurrencyFormat(),
                    days
                )

                if (order.smart_id_creator_session_id.isNullOrBlank()) {

                    renterSignImage.postValue(R.drawable.ic_guide_red)

                    renterSignText.postValue(resourcesRepository.getString(R.string.text_please_sign_the_contract))


                } else {

                    renterSignText.postValue(resourcesRepository.getString(R.string.text_you_signed_the_contract))
                    renterSignImage.postValue(R.drawable.ic_guide_success)
                }

                if (order.smart_id_owner_session_id.isNullOrBlank()) {

                    listerSignText.postValue(
                        String.format(
                            resourcesRepository.getString(R.string.text_waite_for_sign),
                            nameFormat(
                                order.productOwner?.first_name,
                                order.productOwner?.last_name
                            )

                        )
                    )
                    lenderSignImage.postValue(R.drawable.ic_guide_red)
                } else {

                    listerSignText.postValue(
                        String.format(
                            resourcesRepository.getString(R.string.text_signed_the_contract),
                            nameFormat(
                                order.productOwner?.first_name,
                                order.productOwner?.last_name
                            )
                        )
                    )
                    lenderSignImage.postValue(R.drawable.ic_guide_success)
                }


                startRatingVisibility.value = View.GONE

                actionVisibility.value = View.GONE

                acceptVisibility.value = View.GONE
                declineVisibility.value = View.GONE
                finishVisibility.value = View.GONE
                startRentingVisibility.value = View.GONE
                cancelVisibility.value = View.GONE

                userContactVisibility.value = View.GONE

                contractVisibility.postValue(View.GONE)




                when (Order.OrderStatusEnum[order.status]) {

                    Order.OrderStatusEnum.Accepted -> {

                        actionVisibility.value = View.VISIBLE

                        startRentingVisibility.value = View.VISIBLE
                        cancelVisibility.value = View.VISIBLE

                        userContactVisibility.value = View.VISIBLE

                        contractVisibility.postValue(View.VISIBLE)

                    }
                    Order.OrderStatusEnum.Started -> {

                        contractVisibility.postValue(View.VISIBLE)

                        userContactVisibility.value = View.VISIBLE


                    }
                    Order.OrderStatusEnum.Canceled -> {


                    }
                    Order.OrderStatusEnum.Finished -> {

                        actionVisibility.value = View.VISIBLE

                        userContactVisibility.value = View.VISIBLE

                        startRatingVisibility.value = View.VISIBLE

                        contractVisibility.postValue(View.VISIBLE)


                    }
                    Order.OrderStatusEnum.Rejected -> {


                    }
                    Order.OrderStatusEnum.Pending -> {


                        actionVisibility.value = View.VISIBLE

                        cancelVisibility.value = View.VISIBLE

                    }
                    Order.OrderStatusEnum.None -> {


                    }
                }

            }
            Order.OrderTypeEnum.Renting -> {
                order.orderUser?.let {

                    userName.value = nameFormat(it.first_name, it.last_name)



                    it.avatar?.let { image -> userImage.value = BASE_URL + image }

                    userInfoVisibility.value = View.VISIBLE
                } ?: run {
                    userInfoVisibility.value = View.GONE

                }


                var total = 0.0
                order.price?.forEach {
                    if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.ProductFee)
                        total = if (it.price == 0.0) 0.0 else it.price / 100.0
                }


                dayNumber.value = String.format(
                    resourcesRepository.getString(R.string.text_for_day),
                    resourcesRepository.getString(R.string.text_you_earn),
                    total.toCurrencyFormat(),
                    days
                )





                if (order.smart_id_owner_session_id.isNullOrBlank()) {

                    lenderSignImage.postValue(R.drawable.ic_guide_red)

                    listerSignText.postValue(resourcesRepository.getString(R.string.text_please_sign_the_contract))

                    listerSignVisibility.postValue(View.VISIBLE)

                } else {

                    listerSignVisibility.postValue(View.GONE)
                    listerSignText.postValue(resourcesRepository.getString(R.string.text_you_signed_the_contract))
                    lenderSignImage.postValue(R.drawable.ic_guide_success)
                }

                if (order.smart_id_creator_session_id.isNullOrBlank()) {

                    renterSignText.postValue(
                        String.format(
                            resourcesRepository.getString(R.string.text_waite_for_sign),
                            nameFormat(order.orderUser?.first_name, order.orderUser?.last_name)


                        )
                    )
                    renterSignImage.postValue(R.drawable.ic_guide_red)
                } else {

                    renterSignText.postValue(
                        String.format(
                            resourcesRepository.getString(R.string.text_signed_the_contract),
                            nameFormat(order.orderUser?.first_name, order.orderUser?.last_name)
                        )
                    )
                    renterSignImage.postValue(R.drawable.ic_guide_success)
                }







                startRatingVisibility.value = View.GONE

                actionVisibility.value = View.GONE
                recieptVisibility.value = View.GONE
                startRentingVisibility.value = View.GONE
                acceptVisibility.value = View.GONE
                declineVisibility.value = View.GONE
                finishVisibility.value = View.GONE
                cancelVisibility.value = View.GONE

                userContactVisibility.value = View.GONE

                contractVisibility.postValue(View.GONE)

                when (Order.OrderStatusEnum[order.status]) {
                    Order.OrderStatusEnum.Accepted -> {

                        actionVisibility.value = View.VISIBLE
                        cancelVisibility.value = View.VISIBLE

                        contractVisibility.postValue(View.VISIBLE)

                    }
                    Order.OrderStatusEnum.Started -> {

                        actionVisibility.value = View.VISIBLE

                        finishVisibility.value = View.VISIBLE
                        userContactVisibility.value = View.VISIBLE

                        contractVisibility.postValue(View.VISIBLE)
                    }
                    Order.OrderStatusEnum.Canceled -> {

                        listerSignVisibility.postValue(View.GONE)

                    }
                    Order.OrderStatusEnum.Finished -> {

                        listerSignVisibility.postValue(View.GONE)
                        contractVisibility.postValue(View.VISIBLE)

                    }
                    Order.OrderStatusEnum.Rejected -> {

                        listerSignVisibility.postValue(View.GONE)
                    }
                    Order.OrderStatusEnum.Pending -> {

                        listerSignVisibility.postValue(View.GONE)
                        actionVisibility.value = View.VISIBLE

                        acceptVisibility.value = View.VISIBLE
                        declineVisibility.value = View.VISIBLE
                    }
                    Order.OrderStatusEnum.None -> {

                    }
                }

            }
            else -> {
                actionVisibility.value = View.GONE
            }


        }


        val ownerName =
            nameFormat(order.productOwner?.first_name, order.productOwner?.last_name)


        val orderedUserName =
            nameFormat(order.orderUser?.first_name, order.orderUser?.last_name)



        when (Order.OrderStatusEnum[order.status]) {
            Order.OrderStatusEnum.Accepted -> {

                orderStatusImage.value = R.drawable.ic_guide_success

                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.value =
                        String.format(
                            resourcesRepository.getString(R.string.text_rent_request_accepted),
                            orderedUserName
                        )
                } else {

                    orderStatus.value =
                        String.format(
                            resourcesRepository.getString(R.string.text_lend_request_accepted),
                            ownerName
                        )
                }


            }
            Order.OrderStatusEnum.Started -> {

                orderStatusImage.value = R.drawable.ic_guide_success

                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.value =
                        String.format(
                            resourcesRepository.getString(R.string.text_rent_request_started),
                            orderedUserName
                        )
                } else {

                    orderStatus.value =
                        String.format(
                            resourcesRepository.getString(R.string.text_lend_request_started),
                            ownerName
                        )
                }


            }
            Order.OrderStatusEnum.Canceled -> {

                orderStatusImage.value = R.drawable.ic_remove_red

                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = resourcesRepository.getString(R.string.text_rent_canceled)
                } else {

                    orderStatus.value = resourcesRepository.getString(R.string.text_lend_canceled)
                }


            }
            Order.OrderStatusEnum.Finished -> {

                orderStatusImage.value = R.drawable.ic_guide_success


                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = resourcesRepository.getString(R.string.text_rent_complete)
                } else {

                    orderStatus.value = resourcesRepository.getString(R.string.text_lend_complete)
                }
            }
            Order.OrderStatusEnum.Pending -> {

                orderStatusImage.value = R.drawable.ic_pending


                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value =
                        resourcesRepository.getString(R.string.text_rent_request_pending)
                } else {
                    orderStatus.value =
                        String.format(
                            resourcesRepository.getString(R.string.text_lend_request_pending),
                            ownerName
                        )
                }

            }

            Order.OrderStatusEnum.Rejected -> {

                orderStatusImage.value = R.drawable.ic_remove_red


                if (Order.OrderTypeEnum[order.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.value = resourcesRepository.getString(R.string.text_rent_rejected)
                } else {

                    orderStatus.value = resourcesRepository.getString(R.string.text_lend_rejected)
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


    fun onClickStartRenting() {

        startRenting.value = true


    }

    fun onClickStartRate() {

        startRate.value = true
    }

    fun onClickFinished() {

        finishIntro.value = true


    }

    fun onListerSign() {

        signContractShow.postValue(true)


    }


    fun onContractPreview() {

        contractPreview.postValue(true)
    }

    fun onViewContractEN() {


        mOrder.signPdf_EN?.let {

            getPDF(BASE_URL + it, mOrder.suid)


        }

    }

    fun onViewContractLT() {


        mOrder.signPdf_LT?.let {

            getPDF(BASE_URL + it, mOrder.suid)


        }

    }

}