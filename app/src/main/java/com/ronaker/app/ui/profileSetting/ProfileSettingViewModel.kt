package com.ronaker.app.ui.profileSetting


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileSettingViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    fun loadData() {

//        subscription = userRepository
//            .getUserInfo()
//
//            .doOnSubscribe {
//                retry.value = null
//                loading.value = true
//            }
//            .doOnTerminate {
//                loading.value = false
//            }
//
//            .subscribe { result ->
//                if (result.isSuccess()) {
//                   mUser= result.data?.toUser()
//                    signComplete.value = result.data?.is_email_verified
//
//                    phoneComplete.value = result.data?.is_phone_number_verified
//
//                    peymentComplete.value = result.data?.is_payment_info_verified
//                    identityComplete.value = result.data?.is_identity_info_verified
//
//                    imageComplete.value = result.data?.avatar != null
//
//
//                } else {
//                    retry.value = result.error?.detail
//                }
//            }


    }


    fun onRetry() {
//        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


    fun logout() {
        userRepository.clearLogin()
    }

    fun getTermUrl(): String {


        return userRepository.getTermUrl()
    }

    fun getPrivacyUrl(): String {

       return userRepository.getPrivacyUrl()
    }


}