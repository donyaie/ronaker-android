package com.ronaker.app.ui.loginForget

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.AnalyticsManager
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.actionSignUp
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginForgetViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit var context: Context

    @Inject
    lateinit var userRepository: UserRepository


    var forgetSubscription:Disposable?=null

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()
    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val keyboardDown: MutableLiveData<Boolean> = MutableLiveData()


    var resetToken: String? = null


    init {

        if(userRepository.isLogin()){
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
                errorMessage.postValue(context.getString(R.string.text_repeated_password_not_match))
            }
        }

    }


    private fun resetPassword(token: String, password: String) {
        forgetSubscription?.dispose()
        forgetSubscription =
            userRepository.forgetPasswordConfirm(token,password).doOnSubscribe { loadingButton.postValue(true)  }
                .doOnTerminate { loadingButton.postValue(false)  }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        goNext.postValue(true)
                    } else {
                        errorMessage.postValue(result.error?.message)
                    }
                }


    }
    fun setToken(token: String) {

        resetToken = token


    }

    override fun onCleared() {
        forgetSubscription?.dispose()
        super.onCleared()
    }


}