package com.ronaker.app.ui.profilePayment


import android.app.Application
import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.TERMS_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfilePaymentViewModel(val app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository


    @Inject
    lateinit
    var paymentInfoRepository: PaymentInfoRepository

    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
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

    val postalCodeText: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null


    fun onClickTerms(){
        IntentManeger.openUrl(app,TERMS_URL)
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

        cardTypeImage.value= PaymentCard.CardType.detectFast(cardNumber).resource


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


        val year= expireInput?.substring(0,1)
        val month= expireInput?.substring(2,3)


      val payment=  PaymentCard (

          cardNumber =   cardEdit,
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
            .addPaymentInfo(userRepository.getUserToken(),payment)

            .doOnSubscribe {
                loading.value = true
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    goNext.value=true


                } else {
                    errorMessage.value = result.error?.message
                }
            }






    }


}