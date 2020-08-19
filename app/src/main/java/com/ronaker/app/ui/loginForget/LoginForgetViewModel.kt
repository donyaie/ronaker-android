package com.ronaker.app.ui.loginForget

import android.app.Application
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.isNumeric
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginForgetViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val resourcesRepository: ResourcesRepository
) : BaseViewModel() {



    var forgetSubscription: Disposable? = null

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()
    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val keyboardDown: MutableLiveData<Boolean> = MutableLiveData()


    val passwordLengthVisibility: MutableLiveData<Int> = MutableLiveData()
    val passwordAlphabetVisibility: MutableLiveData<Int> = MutableLiveData()
    val passwordMatchVisibility: MutableLiveData<Int> = MutableLiveData()

    var resetToken: String? = null


    init {

        if (userRepository.isLogin()) {
            goNext.postValue(true)
        }
    }


    fun onClickResetPassword(password: String, isValid: Boolean, repeat: String) {
        keyboardDown.postValue(true)
        if (isValid) {
            if (password.compareTo(repeat) == 0) {

                resetToken?.let {
                    resetPassword(it, password)

                }


            } else {
                errorMessage.postValue(resourcesRepository.getString(R.string.text_repeated_password_not_match))
            }
        }

    }


    private fun resetPassword(token: String, password: String) {
        forgetSubscription?.dispose()
        forgetSubscription =
            userRepository.forgetPasswordConfirm(token, password)
                .doOnSubscribe { loadingButton.postValue(true) }
                .doOnTerminate { loadingButton.postValue(false) }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        goNext.postValue(true)
                    } else {

                        if (result.error?.password != null) {

                            val error = StringBuilder()

                            result.error.password?.forEach {

                                error.append(it)
                                error.append('\n')


                            }
                            errorMessage.postValue(error.toString())
                        } else
                            errorMessage.postValue(result.error?.message)
                    }
                }


    }

    fun validatePassword(password: String?, repeat: String): Boolean {
        var isValid = true




        if (password.isNullOrBlank() || password.length < 8) {
            passwordLengthVisibility.postValue(View.VISIBLE)
            isValid = false
        } else
            passwordLengthVisibility.postValue(View.GONE)

        if (password?.trim()?.isNumeric() == true) {
            passwordAlphabetVisibility.postValue(View.VISIBLE)
            isValid = false
        } else
            passwordAlphabetVisibility.postValue(View.GONE)

        if (password?.compareTo(repeat) != 0) {
            passwordMatchVisibility.postValue(View.VISIBLE)
            isValid = false
        } else
            passwordMatchVisibility.postValue(View.GONE)


        AppDebug.log("validatePassword","password: $password , repeat: $repeat isValid: $isValid")

        return isValid
    }


    fun setToken(token: String) {

        resetToken = token


    }

    override fun onCleared() {
        forgetSubscription?.dispose()
        super.onCleared()
    }


}