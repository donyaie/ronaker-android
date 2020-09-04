package com.ronaker.app.ui.profilePayment


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.PaymentCard
import io.reactivex.disposables.Disposable

class ProfilePaymentViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val paymentInfoRepository: PaymentInfoRepository
)  : BaseViewModel() {




    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val cardTypeImage: MutableLiveData<Int> = MutableLiveData()
    val cardNumberText: MutableLiveData<String> = MutableLiveData()
    val cardExpireText: MutableLiveData<String> = MutableLiveData()
    val cardCVVText: MutableLiveData<String> = MutableLiveData()


    val addressPostalText: MutableLiveData<String> = MutableLiveData()
    val countryText: MutableLiveData<String> = MutableLiveData()
    val cityText: MutableLiveData<String> = MutableLiveData()
    val addressLine2Text: MutableLiveData<String> = MutableLiveData()
    val addressText: MutableLiveData<String> = MutableLiveData()
    val fullNameText: MutableLiveData<String> = MutableLiveData()



    private var subscription: Disposable? = null


    fun onClickTerms() {
    }


    fun getTermUrl(): String {


        return userRepository.getTermUrl()
    }



    fun loadData() {

//        subscription = userRepository
//            .getUserInfo()
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

        cardTypeImage.value = PaymentCard.CardType.detectFast(cardNumber).resource


    }

    fun save(
        cardEdit: String?,
        expireInput: String?,
        cvvInput: String?,
        nameInput: String?,
        addressInput: String?,
        addressLine2Input: String?,
        cityInput: String?,
        countryInput: String?,
        addressPostalInput: String?
    ) {


        val year = expireInput?.substring(0, 1)
        val month = expireInput?.substring(2, 3)


        val payment = PaymentCard(

            cardNumber = cardEdit,
            cvv = cvvInput,
            postalCode = addressPostalInput,
            fullName = nameInput,
            address = addressInput,
            address2 = addressLine2Input,
            city = cityInput,
            region = countryInput,
            country = countryInput,
            expiryYear = year,
            expiryMonth = month,
            paymentInfoType = PaymentCard.PaymentType.CreditCard.key

        )




        subscription = paymentInfoRepository
            .addPaymentInfo( payment)

            .doOnSubscribe {
                loadingButton.value = true
            }
            .doOnTerminate {
                loadingButton.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    goNext.value = true


                } else {
                    errorMessage.value = result.error?.message
                }
            }


    }


}