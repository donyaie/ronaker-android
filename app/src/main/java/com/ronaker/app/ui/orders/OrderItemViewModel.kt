package com.ronaker.app.ui.orders

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Order
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity
import com.ronaker.app.utils.BASE_URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OrderItemViewModel : BaseViewModel() {
    private val productTitle = MutableLiveData<String>()
    private val productPrice = MutableLiveData<String>()
    private val productImage = MutableLiveData<String>()
    private val productDate = MutableLiveData<String>()
    private val orderStatus = MutableLiveData<String>()
    private val orderStatusImage = MutableLiveData<Int>()

    lateinit var data: Order
    lateinit var activity: DashboardActivity

    fun bind(item: Order, context: DashboardActivity) {
        data = item
        activity = context
        productTitle.value = item.product.name



        val days = TimeUnit.DAYS.convert(
            data.toDate.time - data.fromDate.time,
            TimeUnit.MILLISECONDS
        )




        productPrice.value = String.format("%s%.02f", context.getString(R.string.title_curency_symbol), (data.product.price_per_day ?: 0.toDouble()) * days)
//        productPrice.value = String.format("%s%.02f", context.getString(R.string.title_curency_symbol), item.price)
        productImage.value = BASE_URL + item.product.avatar
        productDate.value =
            SimpleDateFormat("dd MMM", Locale.getDefault()).format(item.fromDate) + "-" + SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            ).format(item.toDate)


        val ownerName=(item.productOwner?.first_name?:"")+" "+(item.productOwner?.last_name?:"")

        val userName=(item.orderUser?.first_name?:"")+" "+(item.orderUser?.last_name?:"")


        when (Order.OrderStatusEnum.get(item.status)) {
            Order.OrderStatusEnum.Accepted -> {

                orderStatusImage.value=R.drawable.ic_guide_success

                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Renting){


                    orderStatus.value=  activity.getString(R.string.text_rent_request_accepted,userName)
                }else{

                    orderStatus.value=  activity.getString(R.string.text_lend_request_accepted,ownerName)
                }


            }
            Order.OrderStatusEnum.Started -> {

                orderStatusImage.value=R.drawable.ic_guide_success

                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Renting){


                    orderStatus.value=  activity.getString(R.string.text_rent_request_started,userName)
                }else{

                    orderStatus.value=  activity.getString(R.string.text_lend_request_started,ownerName)
                }


            }
            Order.OrderStatusEnum.Canceled -> {

                orderStatusImage.value=R.drawable.ic_remove_red

                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Renting){

                    orderStatus.value=activity.getString(R.string.text_rent_canceled)
                }else{

                    orderStatus.value=activity.getString(R.string.text_lend_canceled)
                }



            }
            Order.OrderStatusEnum.Finished -> {

                orderStatusImage.value=R.drawable.ic_guide_success


                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Renting){

                    orderStatus.value=activity.getString(R.string.text_rent_complete)
                }else{

                    orderStatus.value=activity.getString(R.string.text_lend_complete)
                }
            }
            Order.OrderStatusEnum.Pending -> {

                orderStatusImage.value=R.drawable.ic_pending


                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Renting){

                    orderStatus.value=activity.getString(R.string.text_rent_request_pending)
                }else{
                    orderStatus.value=activity.getString(R.string.text_lend_request_pending,ownerName)
                }

            }

            Order.OrderStatusEnum.Rejected -> {

                orderStatusImage.value=R.drawable.ic_remove_red


                if(Order.OrderTypeEnum[item.orderType] ==Order.OrderTypeEnum.Renting){

                    orderStatus.value=activity.getString(R.string.text_rent_rejected)
                }else{

                    orderStatus.value=activity.getString(R.string.text_lend_rejected)
                }

            }
            else->{

                orderStatusImage.value=R.drawable.ic_remove_red

                orderStatus.value="Not Set"
            }


        }


    }

    fun onClickProduct() {


        activity.startActivityMakeScene(OrderPreviewActivity.newInstance(activity, data) )
    }

    fun getProductTitle(): MutableLiveData<String> {
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
}