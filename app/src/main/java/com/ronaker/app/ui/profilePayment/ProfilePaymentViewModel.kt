package com.ronaker.app.ui.profilePayment


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import java.util.regex.Pattern
import javax.inject.Inject

class ProfilePaymentViewModel : BaseViewModel() {


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val cardTypeImage: MutableLiveData<Int> = MutableLiveData()
    val cardNumberText: MutableLiveData<String> = MutableLiveData()
    val cardExpireText: MutableLiveData<String> = MutableLiveData()
    val cardCVVText: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null


    enum class CardType {

        UNKNOWN,
        VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$", "^4[0-9]$"),
        MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$", "^5[1-5]$"),
        AMERICAN_EXPRESS("^3[47][0-9]{13}$", "^3[47]$"),
        DINERS_CLUB("^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{12,15}$", null),
        DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$", "^6(?:011|5[0-9]{2})$"),
        JCB("^(?:2131|1800|35\\d{3})\\d{11}$", null),
        CHINA_UNION_PAY("^62[0-9]{14,17}$", null);

        private var pattern: Pattern? = null
        private var shortPattern: Pattern? = null

        private constructor() {
            this.pattern = null
        }

        private constructor(pattern: String?, shortPattern: String?) {
            pattern?.let { this.pattern = Pattern.compile(it) }

            shortPattern?.let { this.shortPattern = Pattern.compile(it) }
        }

        companion object {

            fun detect(cardNumber: String): CardType {

                for (cardType in CardType.values()) {
                    if (null == cardType.pattern) continue
                    if (cardType.pattern!!.matcher(cardNumber).matches()) return cardType
                }

                return UNKNOWN
            }

            fun detectFast(cardNumber: String): CardType {


                if (cardNumber.length >= 2)
                    for (cardType in CardType.values()) {
                        if (null == cardType.shortPattern) continue
                        if (cardType.shortPattern!!.matcher(cardNumber.substring(0,2)).matches()) return cardType
                    }


                for (cardType in CardType.values()) {
                    if (null == cardType.pattern) continue
                    if (cardType.pattern!!.matcher(cardNumber).matches()) return cardType
                }

                return UNKNOWN
            }
        }

    }

    init {

    }


    fun loadData() {

//        subscription = userRepository
//            .getUserInfo(userRepository.getUserToken())
//
//            .doOnSubscribe {
//                retry.value = false
//                loading.value = true
//            }
//            .doOnTerminate {
//                loading.value = false
//            }
//
//            .subscribe { result ->
//                if (result.isSuccess()) {
//
//
//
//                } else {
//                    retry.value = true
//                    errorMessage.value = result.error?.detail
//                }
//            }


    }


    fun onRetry() {
        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun changeCardNumber(cardNumber: String) {


        when (CardType.detectFast(cardNumber)) {
            CardType.MASTERCARD -> {

                cardTypeImage.value = R.drawable.ic_card_master
            }
            CardType.DINERS_CLUB -> {

                cardTypeImage.value = R.drawable.ic_card_diners
            }
            CardType.JCB -> {

                cardTypeImage.value = R.drawable.ic_card_amex
            }
            CardType.DISCOVER -> {

                cardTypeImage.value = R.drawable.ic_card_discover
            }
            CardType.VISA -> {

                cardTypeImage.value = R.drawable.ic_card_visa
            }
            else -> {

                cardTypeImage.value = R.drawable.ic_card_empty
            }


        }


    }


}