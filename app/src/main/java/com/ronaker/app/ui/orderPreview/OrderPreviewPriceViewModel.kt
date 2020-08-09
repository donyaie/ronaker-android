package com.ronaker.app.ui.orderPreview

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.model.Order
import com.ronaker.app.utils.toCurrencyFormat
import javax.inject.Inject

class OrderPreviewPriceViewModel(app: Application) : BaseViewModel(app) {
    val title = MutableLiveData<String>()
    val price = MutableLiveData<String>()

    @Inject
    lateinit
    var resourcesRepository: ResourcesRepository


    fun bind(
        data: Order.OrderPrices
    ) {


        val mPrice = (if (data.price == 0.0) 0.0 else data.price / 100.0)



        price.value = mPrice.toCurrencyFormat()
//        Price.value = String.format(
//            "%s%.02f",
//            app.getString(R.string.title_curency_symbol),
//            mPrice
//        )


        when (Order.OrderPriceEnum[data.key]) {
            Order.OrderPriceEnum.ServiceFee -> {

                title.value = resourcesRepository.getString(R.string.text_service_fee)
            }
            Order.OrderPriceEnum.InsuranceFee -> {

                title.value = resourcesRepository.getString(R.string.text_insurance_fee)
            }
            Order.OrderPriceEnum.ProductFee -> {

                title.value = resourcesRepository.getString(R.string.text_product_fee)
            }
            Order.OrderPriceEnum.Total -> {

                title.value = resourcesRepository.getString(R.string.text_total_fee)
            }
            else -> {

                title.value = data.key
            }


        }


    }


}