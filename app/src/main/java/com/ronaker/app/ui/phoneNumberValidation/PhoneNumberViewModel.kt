package com.ronaker.app.ui.phoneNumberValidation

import android.app.Application
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject



class PhoneNumberViewModel (app: Application): BaseViewModel(app) {

    internal val TAG = PhoneNumberViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    private var addPhoneSubscription: Disposable? = null

    private var resendSubscription: Disposable? = null

    private var verifyPhoneSubscription: Disposable? = null


    val timerVisibility: MutableLiveData<Int> = MutableLiveData()
    val resendVisibility: MutableLiveData<Int> = MutableLiveData()
    val timerValue: MutableLiveData<String> = MutableLiveData()




    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    enum class StateEnum constructor(position: Int) {
        number(0),
        validate(1);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = number
                for (stateEnum in values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }

    var mNumber = ""


    fun onClickPhoneNext(phone: String) {

        addPhoneSubscription = userRepository.addUserPhoneNumber(userRepository.getUserToken(), phone)
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                loading.value = false
                if (result.isSuccess()) {
                    mNumber = phone
                    viewState.value = StateEnum.validate
                    startTimer()

                } else {
                    errorMessage.value = result.error?.detail
                }
            }

    }
    fun resend(){
        resendSubscription = userRepository.addUserPhoneNumber(userRepository.getUserToken(), mNumber)
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                loading.value = false
                if (result.isSuccess()) {
                    startTimer()

                } else {
                    errorMessage.value = result.error?.detail
                }
            }
    }

    var  countDounTimer: CountDownTimer?=null

    fun startTimer(){
        timerValue.value="00"
        timerVisibility.value= View.VISIBLE
        resendVisibility.value= View.GONE

        countDounTimer=  object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerValue.value= (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                timerVisibility.value= View.GONE
                resendVisibility.value= View.VISIBLE
            }
        }.start()

    }

    fun onClickValidNext(pin: String) {

        verifyPhoneSubscription = userRepository.activeUserPhoneNumber(userRepository.getUserToken(), mNumber, pin)
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                loading.value = false
                if (result.isSuccess()) {
                    goNext.value = false
                } else {
                    errorMessage.value = result.error?.detail
                }
            }

    }


    val viewState: MutableLiveData<StateEnum> = MutableLiveData()


    init {

    }


    override fun onCleared() {
        super.onCleared()
        addPhoneSubscription?.dispose()
        verifyPhoneSubscription?.dispose()
        resendSubscription?.dispose()

        countDounTimer?.cancel()
    }


}