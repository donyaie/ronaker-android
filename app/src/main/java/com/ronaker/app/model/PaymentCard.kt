package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.R
import com.ronaker.app.data.network.request.PaymentInfoCreateRequestModel
import com.ronaker.app.data.network.response.PaymentInfoListResponseModel
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.regex.Pattern


@Parcelize
data class PaymentCard(
    var suid: String? = null,
    val cardNumber: String? = null,
    val expiryMonth: String? = null,
    val expiryYear: String? = null,
    val postalCode: String? = null,
    val fullName: String? = null,
    val address: String? = null,
    val address2: String? = null,
    val country: String? = null,
    val region: String? = null,
    val city: String? = null,
    val cvv: String? = null,
    val paymentInfoType: String? = null,
    val isVerified: Boolean? = null
) : Parcelable {

    @IgnoredOnParcel
    var selected = false


    //https://gist.github.com/michaelkeevildown/9096cd3aac9029c4e6e05588448a8841
    //https://en.wikipedia.org/wiki/Payment_card_number
    //https://github.com/bendrucker/creditcards-types/tree/master/types


    enum class PaymentType(key: String) {

        PayPal("paypal"),
        CreditCard("credit_cart"),
        Cash("cash"),
        UNKNOWN("");


        var key: String = key
            internal set


        companion object {


            operator fun get(position: String): PaymentType {
                var state = UNKNOWN
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }


    }


    enum class CardType {

        UNKNOWN(
            "",
            null,
            null,
            R.drawable.ic_card_empty
        ),
        //        CreditCard("^[0-9]\\d{13,16}$",
//            "^[0-9]\\d{13,16}$",
//            R.drawable.ic_card_empty),
        VISAMASTER(
            "VISAMASTER",
            "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})$",
            "^(?:4[0-9]{0,12}(?:[0-9]{3})?|5[1-5][0-9]{0,14})$",
            R.drawable.ic_card_visa
        ),
        VISA(
            "VISA",
            "^4[0-9]{12}(?:[0-9]{3})?$",
            "^4[0-9]{0,12}(?:[0-9]{3})?$",
            R.drawable.ic_card_visa
        ),
        MASTERCARD(
            "MASTERCARD",
            "^5[1-5][0-9]{14}$",
            "^5[1-5][0-9]{0,14}$",
            R.drawable.ic_card_master
        ),
        MaestroCARD(
            "MaestroCARD",
            "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{8,15}$",
            "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{0,15}$",
            R.drawable.ic_card_master
        ),
        DINERS_CLUB(
            "DINERS_CLUB",
            "^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})[0-9]{12,15}$",
            "^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})[0-9]{0,15}$",
            R.drawable.ic_card_diners
        ),
        DISCOVER(
            "DISCOVER",
            "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$",
            "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{0,10})$",
            R.drawable.ic_card_discover
        ),
        Dankort(
            "Dankort",
            "^(?:5019|4571)[0-9]{12}$",
            "^(?:5019|4571)[0-9]{0,12}$",
            R.drawable.ic_card_empty
        ),
        CarteBleue(
            "CarteBleue",
            "^389[0-9]{11}$",
            "^389[0-9]{0,11}$",
            R.drawable.ic_card_empty
        ),
        CartaSi(
            "CartaSi",
            "^(?:432917|432930|453998)[0-9]{10}$",
            "^(?:432917|432930|453998)[0-9]{0,10}$",
            R.drawable.ic_card_empty
        ),

//        AMERICAN_EXPRESS(
//            "^3[47][0-9]{13}$", "^3[47][0-9]{0,13}$",
//            R.drawable.ic_card_empty
//        ),
//        CHINA_UNION_PAY(
//            "^62[0-9]{14,17}$", "^62[0-9]{0,17}$",
//            R.drawable.ic_card_empty
//        ),
//        JCB("^(?:2131|1800|35\\d{3})\\d{11}$",
//            "^(?:2131|1800|35\\d{3})\\d{0,11}$",
//            R.drawable.ic_card_empty),


        ;

        var key: String = ""
            internal set
        var pattern: Pattern? = null
            internal set
        var shortPattern: Pattern? = null
            internal set
        var resource: Int = 0
            internal set


        constructor() {
            this.pattern = null
        }


        constructor(key: String, pattern: String?, shortPattern: String?, resource: Int) {

            this.key = key
            this.resource = resource


            pattern?.let { this.pattern = Pattern.compile(it) }
            shortPattern?.let {
                this.shortPattern =
                    Pattern.compile(it)
            }

        }

        companion object {


            operator fun get(position: String): CardType {
                var state = UNKNOWN
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }

            fun detect(cardNumber: String): CardType {
                for (cardType in values()) {
                    cardType.pattern?.let {
                        if (it.matcher(cardNumber).matches())
                            return cardType
                    }
                }

                return UNKNOWN
            }

            fun detectFast(cardNumber: String): CardType {


                var type = UNKNOWN

                for (cardType in values()) {

                    cardType.shortPattern?.let {
                        if (
                            it.matcher(cardNumber).matches()
                        )
                            type = cardType
                    }
                }



                for (cardType in values()) {
                    cardType.pattern?.let {
                        if (it.matcher(cardNumber).matches())
                            type = cardType
                    }
                }

                return type
            }
        }

    }
}


fun List<PaymentInfoListResponseModel>.toPaymentInfoList(): List<PaymentCard> {

    val list: ArrayList<PaymentCard> = ArrayList()

    this.forEach {

        val value = PaymentCard(
            it.suid,
            it.card_number,
            it.expiry_month,
            it.expiry_year,
            it.postal_code,
            it.full_name,
            it.address,
            it.address_2,
            it.country,
            it.region,
            it.city,
            it.cvv,
            it.payment_info_type,
            it.is_verified
        )

        list.add(value)
    }

    return list

}


fun PaymentCard.toPaymentCardCreateModel(): PaymentInfoCreateRequestModel {

    return PaymentInfoCreateRequestModel(

        this.cardNumber,
        this.expiryMonth,
        this.expiryYear,
        this.postalCode,
        this.fullName,
        this.address,
        this.address2,
        this.country,
        this.region,
        this.city,
        this.cvv,
        this.paymentInfoType


    )

}
