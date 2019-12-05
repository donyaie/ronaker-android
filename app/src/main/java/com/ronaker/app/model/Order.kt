package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.OrderResponseModel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Order(
    val suid: String,
    val message: String,
    val fromDate: Date,
    val toDate: Date,
    val price: Double,
    val orderType: String,
    val status: String,
    val product: Product,
    val productOwner: User?,
    val orderUser: User?,
    val rejectionReason: String?,
    val address: String?,
    val instruction: String?
) : Parcelable {

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

}


fun List<OrderResponseModel>.toOrderList(): List<Order> {


    val list: ArrayList<Order> = ArrayList()

    this.forEach {

            val product = Order(
                it.suid,
                it.message,
                it.start_date,
                it.end_date,
                it.price,
                it.order_type,
                it.status,
                it.product.toProduct(),
                it.product_owner?.toUserModel(),
                it.order_user?.toUserModel(),
                it.rejection_reason,
                it.address,
                it.instruction
            )

            list.add(product)

    }

    return list

}






