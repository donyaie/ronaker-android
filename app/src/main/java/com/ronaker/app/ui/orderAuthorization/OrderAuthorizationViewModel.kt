package com.ronaker.app.ui.orderAuthorization

import android.os.CountDownTimer
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class OrderAuthorizationViewModel @ViewModelInject constructor(
    userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val resourcesRepository: ResourcesRepository
) : BaseViewModel() {


    private var isStartCert = false
    private var isCheckCert = false
    private var canStartRenting = false
    private var defaultLanguage: String
    private var firstStartRenting = true

    val viewState: MutableLiveData<StateEnum> = MutableLiveData()


    val authPinCode: MutableLiveData<String> = MutableLiveData()


    val goNext: MutableLiveData<Boolean> = MutableLiveData()

    val language: MutableLiveData<String> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()
    val startRentVisibility: MutableLiveData<Int> = MutableLiveData()


    val codeLoadingButton: MutableLiveData<Boolean> = MutableLiveData()


    val timerVisibility: MutableLiveData<Int> = MutableLiveData()
    val resendVisibility: MutableLiveData<Int> = MutableLiveData()

    val timerValue: MutableLiveData<String> = MutableLiveData()


    val errorMessageFinal: MutableLiveData<String> = MutableLiveData()

    val errorMessageVisibility: MutableLiveData<Int> = MutableLiveData()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    private var mOrder: Order? = null


    private var countDounTimer: CountDownTimer? = null


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    private var subscriptionVerificationCode: Disposable? = null
    private var startCertSubscription: Disposable? = null
    private var checkCertSubscription: Disposable? = null
    private var startSubscription: Disposable? = null
    private var checkSubscription: Disposable? = null
    private var acceptSubscription: Disposable? = null



    init {
        defaultLanguage = userRepository.getUserLanguage()
        language.postValue(defaultLanguage)
    }


    enum class StateEnum constructor(position: Int) {
        PersonalCode(0),
        Auth(1);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = PersonalCode
                for (stateEnum in values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }

    fun onPersonalCodeNext() {

        startCert()


    }

    private fun startCert() {

        if (isStartCert) {
            startCheckCert()
            return
        }




        startCertSubscription?.dispose()
        startCertSubscription = mOrder?.suid?.let {
            orderRepository.startSmartIDCert(
                orderSuid = it
            )
                .doOnSubscribe { codeLoadingButton.postValue(true) }
                .doOnTerminate {}
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {
                        isStartCert = true
                        startCheckCert()


                    } else {
                        isStartCert = false
                        codeLoadingButton.postValue(false)
                        errorMessage.postValue(result.error?.message)
                    }
                }
        }
    }


    private fun startCheckCert() {

        if (isCheckCert) {
            sendVerificationCode()
            return
        }


        checkCertSubscription?.dispose()
        checkCertSubscription = mOrder?.suid?.let {
            orderRepository.checkSmartIDSessionCert(
                orderSuid = it
            )
                .doOnSubscribe { codeLoadingButton.postValue(true) }
                .doOnTerminate { }
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {
                        isCheckCert = true
                        sendVerificationCode()


                    } else {
                        isCheckCert = false
                        codeLoadingButton.postValue(false)
                        errorMessage.postValue(result.error?.message)
                    }
                }
        }
    }


    private fun sendVerificationCode() {


        subscriptionVerificationCode?.dispose()
        subscriptionVerificationCode = mOrder?.suid?.let {
            orderRepository.getSmartIDVerificationCode(
                orderSuid = it
            )
                .doOnSubscribe { codeLoadingButton.postValue(true) }
                .doOnTerminate { codeLoadingButton.postValue(false) }
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {

                        authPinCode.postValue(result.data)

                        viewState.postValue(StateEnum.Auth)

                        startAuth()


                    } else {

                        errorMessage.postValue(result.error?.message)
                    }
                }
        }
    }


    private fun startAuth() {
        errorMessageVisibility.postValue(View.GONE)
        checkSubscription?.dispose()
        startSubscription?.dispose()
        startSubscription = mOrder?.suid?.let {
            orderRepository.startSmartIDAuth(
                orderSuid = it
            )
                .doOnSubscribe { }
                .doOnTerminate { }
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {

                        startTimer()

                        startCheck()

                        loadingVisibility.postValue(View.VISIBLE)
                        errorMessageVisibility.postValue(View.GONE)


                    } else {
                        stopTimer()
                        loadingVisibility.postValue(View.GONE)
                        errorMessageVisibility.postValue(View.VISIBLE)
                        errorMessageFinal.postValue(result.error?.message)
                    }
                }
        }


    }


    private fun startCheck() {
        checkSubscription?.dispose()
        checkSubscription =
            mOrder?.suid?.let {
                orderRepository.checkSmartIDSession(it)
                    .doOnSubscribe { }
                    .doOnTerminate { }
                    .delay(3, TimeUnit.SECONDS)
                    .subscribe { result ->
                        if (result.isSuccess() || result.isAcceptable()) {
                            if (canStartRenting) {
                                startRenting()
                            } else
                                goNext.postValue(false)
                        } else {

                            if (result.error?.responseCode == 406) {
                                startCheck()

                            } else {
                                stopTimer()
                                loadingVisibility.postValue(View.GONE)
                                errorMessageVisibility.postValue(View.VISIBLE)
                                errorMessageFinal.postValue(result.error?.message)

                            }


                        }
                    }
            }


    }

    fun onClickStartRent() {
        startRenting()
    }


    private fun startRenting() {

        val delay = if (firstStartRenting) 3L else 1L
        firstStartRenting = false

        acceptSubscription?.dispose()
        acceptSubscription = mOrder?.suid?.let {
            orderRepository.updateOrderStatus(
                suid = it,
                status = "started"
            )
                .doOnSubscribe { loadingButton.postValue(true) }
                .doOnTerminate { loadingButton.postValue(false) }
                .delay(delay, TimeUnit.SECONDS)
                .subscribe { result ->
                    if (result.isSuccess() || result.isAcceptable()) {

                        startRentVisibility.postValue(View.GONE)
                        goNext.postValue(true)

                    } else {

                        if (result.error?.responseCode == 406)
                            errorMessage.postValue(resourcesRepository.getString(R.string.text_make_sure_sign_the_contract))
                        else
                            errorMessage.postValue(result.error?.message)


                        startRentVisibility.postValue(View.VISIBLE)
                    }
                }
        }
    }


    fun resend() {
        checkSubscription?.dispose()
        viewState.postValue(StateEnum.PersonalCode)
    }


    private fun startTimer() {
        countDounTimer?.cancel()


        timerValue.postValue("00")
        timerVisibility.postValue(View.VISIBLE)
        resendVisibility.postValue(View.GONE)

        countDounTimer = object : CountDownTimer(120000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerValue.postValue((millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                timerVisibility.postValue(View.GONE)
                resendVisibility.postValue(View.VISIBLE)
                checkSubscription?.dispose()
            }
        }.start()

    }

    private fun stopTimer() {
        countDounTimer?.cancel()

        timerVisibility.postValue(View.GONE)
        resendVisibility.postValue(View.VISIBLE)
        checkSubscription?.dispose()


    }


    override fun onCleared() {
        super.onCleared()
        startSubscription?.dispose()
        subscriptionVerificationCode?.dispose()
        checkSubscription?.dispose()
        countDounTimer?.cancel()
        startCertSubscription?.dispose()
        checkCertSubscription?.dispose()
        acceptSubscription?.dispose()
    }

    fun setOrder(order: Order?) {
        mOrder = order


    }

    fun changeLanguage() {

        defaultLanguage =
            if (defaultLanguage.toLowerCase(Locale.ROOT).trim().compareTo("lt") == 0) {
                "en"
            } else {
                "lt"
            }
        language.postValue(defaultLanguage)

    }


    fun canStartRenting(canStartRenting: Boolean) {
        this.canStartRenting = canStartRenting
    }


}