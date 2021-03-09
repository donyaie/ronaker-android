package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.OrderPriceResponseModel
import com.ronaker.app.data.network.response.OrderResponseModel

import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Order(
    val suid: String,
    val message: String,
    val fromDate: Date,
    val toDate: Date,
    val price: List<OrderPrices>?,
    val orderType: String,
    val status: String,
    val product: Product,
    val productOwner: User?,
    val orderUser: User?,
    val rejectionReason: String?,
    val address: String?,
    val instruction: String?,
    val isArchived: Boolean = false,
    val smart_id_creator_session_id: String?,
    val smart_id_owner_session_id: String?,
    val signPdf_EN: String? ,
    val signPdf_LT: String?
) : Parcelable {


    override fun equals(other: Any?): Boolean {

        if (other is Order)
            return this.hashCode() == other.hashCode()


        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = suid.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + fromDate.hashCode()
        result = 31 * result + toDate.hashCode()
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + orderType.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + product.hashCode()
        result = 31 * result + isArchived.hashCode()
        result = 31 * result + (productOwner?.hashCode() ?: 0)
        result = 31 * result + (orderUser?.hashCode() ?: 0)
        result = 31 * result + (rejectionReason?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (instruction?.hashCode() ?: 0)
        return result
    }

    enum class OrderStatusEnum constructor(key: String) {
        Pending("pending"),
        Accepted("accepted"),
        Started("started"),
        Finished("finished"),
        Canceled("canceled"),
        Rejected("rejected"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String): OrderStatusEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }


    enum class OrderPriceEnum constructor(key: String) {
        ProductFee("product_fee"),
        ServiceFee("service_fee"),
        InsuranceFee("insurance_fee"),
        Total("total"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String): OrderPriceEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }


    enum class OrderTypeEnum constructor(key: String) {
        Lending("lending"),
        Renting("renting"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String): OrderTypeEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }


    @Parcelize
    data class OrderPrices(
        val key: String,
        val price: Double
    ) : Parcelable

}

fun List<OrderResponseModel>.toOrderList(): List<Order> {


    val list: ArrayList<Order> = ArrayList()

    this.forEach {

        val product = it.toOrderModel()
        list.add(product)

    }

    return list

}


fun OrderResponseModel.toOrderModel(): Order {
    return Order(
        suid = suid,
        message = message,
        fromDate = start_date,
        toDate = end_date,
        price = prices?.toOrderPriceList(),
        orderType = order_type,
        status = status,
        product = product.toProduct(),
        productOwner = product_owner?.toUserModel(),
        orderUser = order_user?.toUserModel(),
        rejectionReason = rejection_reason,
        address = address,
        instruction = instruction,
        isArchived = is_archived,
        smart_id_creator_session_id = smart_id_creator_session_id,
        smart_id_owner_session_id = this.smart_id_owner_session_id,
        signPdf_EN = sign_pdf?.en,
        signPdf_LT = sign_pdf?.lt

    )


}

fun List<OrderPriceResponseModel>.toOrderPriceList(): List<Order.OrderPrices> {
    val list: ArrayList<Order.OrderPrices> = ArrayList()

    this.forEach {

        val product = Order.OrderPrices(
            it.key,
            it.price
        )

        list.add(product)

    }

    return list
}






