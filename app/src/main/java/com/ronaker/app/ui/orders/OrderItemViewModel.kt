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
        productPrice.value = String.format("%s%.02f", context.getString(R.string.title_curency_symbol), item.price)
        productImage.value = BASE_URL + item.product.avatar
        productDate.value =
            SimpleDateFormat("dd MMM", Locale.getDefault()).format(item.fromDate) + "-" + SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            ).format(item.toDate)



        when (Order.OrderStatusEnum.get(item.status)) {
            Order.OrderStatusEnum.Accepted -> {

                orderStatusImage.value=R.drawable.ic_guide_success

                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Lending){

                    orderStatus.value="You accepted request"
                }else{

                    orderStatus.value="Your request accepted"
                }


            }
            Order.OrderStatusEnum.Canceled -> {

                orderStatusImage.value=R.drawable.ic_remove_red

                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Lending){

                    orderStatus.value="Lend | Canceled Order"
                }else{

                    orderStatus.value="Rent | Canceled Order"
                }



            }
            Order.OrderStatusEnum.Finished -> {

                orderStatusImage.value=R.drawable.ic_guide_success


                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Lending){

                    orderStatus.value="Lend | Completed Order"
                }else{

                    orderStatus.value="Rent | Completed Order"
                }
            }
            Order.OrderStatusEnum.Pending -> {

                orderStatusImage.value=R.drawable.ic_pending


                if(Order.OrderTypeEnum.get(item.orderType)==Order.OrderTypeEnum.Lending){

                    orderStatus.value="Someone requested to rent your item"
                }else{

                    orderStatus.value="Pending to accept from Owner"
                }

            }
            else->{

                orderStatusImage.value=R.drawable.ic_remove_red

                orderStatus.value="Not Set"
            }


        }


    }

    fun onClickProduct() {


        activity.startActivityMakeScene(data.suid?.let { OrderPreviewActivity.newInstance(activity, it) })
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