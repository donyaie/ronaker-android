package com.ronaker.app.ui.orders

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterOrdreItemBinding
import com.ronaker.app.model.Order
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import com.ronaker.app.utils.nameFormat
import com.ronaker.app.utils.toCurrencyFormat
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

class OrderItemViewModel(
    val binding: AdapterOrdreItemBinding
) {




    private val productTitle = MutableLiveData<String?>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()
    private val productDate = MutableLiveData<String>()
    private val orderStatus = MutableLiveData<String>()
    private val orderStatusImage = MutableLiveData<Int>()
    private val archiveVisibility = MutableLiveData<Int>()
    private val rateVisibility = MutableLiveData<Int>()


    var mListener: OrderItemAdapter.OrderItemListener? = null


    lateinit var data: Order


     fun onCleared() {

        rateVisibility.postValue(View.GONE)
        archiveVisibility.postValue(View.GONE)


    }


    fun bind(item: Order,  listener: OrderItemAdapter.OrderItemListener) {
        data = item
        mListener = listener



        val context = binding.root.getParentActivity()?.baseContext?: binding.root.getApplication()
        productTitle.postValue(
            item.product.name
        )

//        val days = TimeUnit.DAYS.convert(
//            data.toDate.time - data.fromDate.time,
//            TimeUnit.MILLISECONDS
//        )
//
//


        var total = 0.0

        if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {


            data.price?.forEach {
                if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.ProductFee)
                    total = if (it.price == 0.0) 0.0 else it.price / 100.0
            }

        } else {

            data.price?.forEach {
                if (Order.OrderPriceEnum[it.key] == Order.OrderPriceEnum.Total)
                    total = if (it.price == 0.0) 0.0 else it.price / 100.0
            }

        }

        productPrice.postValue(total.toCurrencyFormat())


//        productPrice.postValue(  String.format("%s%.02f", context.getString(R.string.title_curency_symbol), item.Price)
        productImage.postValue(BASE_URL + item.product.avatar)
        productDate.postValue(
            SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            ).format(item.fromDate) + "-" + SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            ).format(item.toDate)
        )


        val ownerName = nameFormat(item.productOwner?.first_name, item.productOwner?.last_name)

        val userName = nameFormat(item.orderUser?.first_name, item.orderUser?.last_name)


        archiveVisibility.postValue(View.GONE)


        rateVisibility.postValue(View.GONE)



        when (Order.OrderStatusEnum[item.status]) {
            Order.OrderStatusEnum.Accepted -> {

                orderStatusImage.postValue(R.drawable.ic_guide_success)

                if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.postValue(
                        String.format(
                            context.getString(
                                R.string.text_rent_request_accepted
                            ),
                            userName
                        )
                    )
                } else {

                    orderStatus.postValue(
                        String.format(
                            context.getString(R.string.text_lend_request_accepted),
                            ownerName
                        )
                    )
                }


            }
            Order.OrderStatusEnum.Started -> {

                orderStatusImage.postValue(
                    R.drawable.ic_guide_success
                )

                if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.postValue(
                        String.format(
                            context.getString(
                                R.string.text_rent_request_started
                            ),
                            userName
                        )
                    )
                } else {

                    orderStatus.postValue(
                        String.format(
                            context.getString(
                                R.string.text_lend_request_started
                            ),
                            ownerName
                        )
                    )
                }


            }
            Order.OrderStatusEnum.Canceled -> {

                orderStatusImage.postValue(
                    R.drawable.ic_remove_red
                )

                if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.postValue(context.getString(R.string.text_rent_canceled))
                } else {

                    orderStatus.postValue(context.getString(R.string.text_lend_canceled))
                }


                if (!item.isArchived)
                    archiveVisibility.postValue(View.VISIBLE)
            }
            Order.OrderStatusEnum.Finished -> {

                orderStatusImage.postValue(
                    R.drawable.ic_guide_success
                )


                if (!item.isArchived)
                    archiveVisibility.postValue(View.VISIBLE)



                if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {


                    orderStatus.postValue(context.getString(R.string.text_rent_complete))
                } else {
                    rateVisibility.postValue(View.VISIBLE)

                    orderStatus.postValue(context.getString(R.string.text_lend_complete))
                }
            }
            Order.OrderStatusEnum.Pending -> {

                orderStatusImage.postValue(
                    R.drawable.ic_pending
                )


                if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.postValue(context.getString(R.string.text_rent_request_pending))
                } else {
                    orderStatus.postValue(
                        String.format(
                            context.getString(
                                R.string.text_lend_request_pending
                            ),
                            ownerName
                        )
                    )
                }

            }

            Order.OrderStatusEnum.Rejected -> {

                orderStatusImage.postValue(
                    R.drawable.ic_remove_red
                )


                if (Order.OrderTypeEnum[item.orderType] == Order.OrderTypeEnum.Renting) {

                    orderStatus.postValue(context.getString(R.string.text_rent_rejected))
                } else {

                    orderStatus.postValue(context.getString(R.string.text_lend_rejected))
                }

                if (!item.isArchived)
                    archiveVisibility.postValue(View.VISIBLE)
            }
            else -> {

                orderStatusImage.postValue(
                    R.drawable.ic_remove_red
                )

                orderStatus.postValue("Not Set")
            }


        }


    }

    fun onClickProduct() {


        mListener?.onClickItem(data)

    }

    var disposable: Disposable? = null

    fun onArchiveClick() {

        mListener?.onClickItemArchive(data)


    }

    fun onRateClick() {

        mListener?.onClickItemRate(data)

//        activity?.let { it.startActivity(ProductRateActivity.newInstance(it, data)) }
    }

    fun getProductTitle(): MutableLiveData<String?> {
        return productTitle
    }

    fun getProductPrice(): MutableLiveData<String> {
        return productPrice
    }

    fun getProductImage(): MutableLiveData<String> {
        return productImage
    }

    fun getProductDate(): MutableLiveData<String> {
        return productDate
    }


    fun getOrderStatus(): MutableLiveData<String> {
        return orderStatus
    }

    fun getOrderStatusImage(): MutableLiveData<Int> {
        return orderStatusImage
    }

    fun getRateVisibility(): MutableLiveData<Int> {
        return rateVisibility
    }

    fun getArchiveVisibility(): MutableLiveData<Int> {
        return archiveVisibility
    }


}