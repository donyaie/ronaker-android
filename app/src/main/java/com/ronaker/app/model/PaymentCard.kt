package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.R
import kotlinx.android.parcel.Parcelize
import java.util.regex.Pattern


@Parcelize
data class PaymentCard(
    var suid: String
    , var title: String
    , var type: String
) : Parcelable {

    //https://gist.github.com/michaelkeevildown/9096cd3aac9029c4e6e05588448a8841
    //https://en.wikipedia.org/wiki/Payment_card_number
    //https://github.com/bendrucker/creditcards-types/tree/master/types

    enum class CardType {

        UNKNOWN(
            null,
            null,
            R.drawable.ic_card_empty
        ),
        //        CreditCard("^[0-9]\\d{13,16}$",
//            "^[0-9]\\d{13,16}$",
//            R.drawable.ic_card_empty),
        VISAMASTER(
            "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})$",
            "^(?:4[0-9]{0,12}(?:[0-9]{3})?|5[1-5][0-9]{0,14})$",
            R.drawable.ic_card_visa
        ),
        VISA(
            "^4[0-9]{12}(?:[0-9]{3})?$",
            "^4[0-9]{0,12}(?:[0-9]{3})?$",
            R.drawable.ic_card_visa
        ),
        MASTERCARD(
            "^5[1-5][0-9]{14}$",
            "^5[1-5][0-9]{0,14}$",
            R.drawable.ic_card_master
        ),
        MaestroCARD(
            "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{8,15}$",
            "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{0,15}$",
            R.drawable.ic_card_master
        ),
        DINERS_CLUB(
            "^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})[0-9]{12,15}$",
            "^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})[0-9]{0,15}$",
            R.drawable.ic_card_diners
        ),
        DISCOVER(
            "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$",
            "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{0,10})$",
            R.drawable.ic_card_discover
        ),
        Dankort(
            "^(?:5019|4571)[0-9]{12}$",
            "^(?:5019|4571)[0-9]{0,12}$",
            R.drawable.ic_card_empty
        ),
        CarteBleue(
            "^389[0-9]{11}$",
            "^389[0-9]{0,11}$",
            R.drawable.ic_card_empty
        ),
        CartaSi(
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

        private var pattern: Pattern? = null
        private var shortPattern: Pattern? = null
        private var recourse: Int? = null

        fun getRecourse(): Int? {
            return recourse
        }

        constructor() {
            this.pattern = null
        }

        constructor(pattern: String?, shortPattern: String?, resource: Int?) {
            pattern?.let { this.pattern = Pattern.compile(it) }

            this.recourse = resource
            shortPattern?.let {
                this.shortPattern =
                    Pattern.compile(it)
            }
        }

        companion object {

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

//                if (cardNumber.length >= 2) {
//
//                    var last = 2
//                    if (cardNumber.length < 6)
//                        last = cardNumber.length

                for (cardType in values()) {

                    cardType.shortPattern?.let {
                        if (it.matcher(
                                cardNumber
/*                                       .substring(
//                                        0,
                                    last
                                    )
*/

                            ).matches()
                        )
                            type = cardType
                    }
                }
//                }


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

