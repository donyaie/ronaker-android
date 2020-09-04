package com.ronaker.app.ui.profileEmailVerify


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable

class ProfileEmailVerifyViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
)  : BaseViewModel() {




    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val openInbox: MutableLiveData<Boolean> = MutableLiveData()


    val successMessage: MutableLiveData<String> = MutableLiveData()
    val goNex: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null
    private var emailVerificationSubscription: Disposable? = null

    fun loadData() {
        subscription?.dispose()
        subscription = userRepository
            .getUserInfo(userRepository.getUserAuthorization())
            .doOnSubscribe {
            }
            .doOnTerminate {
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    if (result?.data?.is_email_verified == true) {
                        goNex.value = true
                    }

                } else {
                    errorMessage.value = result.error?.message
                }
            }


    }


    private fun sendVerificationEmail() {
        emailVerificationSubscription?.dispose()
        emailVerificationSubscription =
            userRepository.sendEmailVerification()
                .doOnSubscribe { loadingButton.value = true }
                .doOnTerminate { loadingButton.value = false }
                .subscribe { result ->
                    if (result.isAcceptable()) {
                        successMessage.value = "Verification Email Sent"
                    } else {
                        errorMessage.value = result.error?.message
                    }


                }
    }


    fun gotoMailClick() {



        openInbox.postValue(true)
    }


    fun resendMailClick() {
        sendVerificationEmail()
    }

    fun onRetry() {
//        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }




}