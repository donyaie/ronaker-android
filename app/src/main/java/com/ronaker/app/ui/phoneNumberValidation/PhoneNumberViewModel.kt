package com.ronaker.app.ui.phoneNumberValidation

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PhoneNumberViewModel : BaseViewModel() {

    internal val TAG = PhoneNumberViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    private var addPhoneSubscription: Disposable? = null
    private var verifyPhoneSubscription: Disposable? = null


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
                for (stateEnum in StateEnum.values()) {
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
                } else {
                    errorMessage.value = result.error?.detail
                }
            }

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
    }


}