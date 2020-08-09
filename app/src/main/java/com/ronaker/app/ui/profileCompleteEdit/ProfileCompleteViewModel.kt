package com.ronaker.app.ui.profileCompleteEdit


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileCompleteViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository



    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val phoneComplete: MutableLiveData<Boolean> = MutableLiveData()
    val imageComplete: MutableLiveData<Boolean> = MutableLiveData()
    val signComplete: MutableLiveData<Boolean> = MutableLiveData()
    val peymentComplete: MutableLiveData<Boolean> = MutableLiveData()
    val identityComplete: MutableLiveData<Boolean> = MutableLiveData()
    val smartIDComplete: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    private var mUser: User? = null

    fun loadData() {

        subscription = userRepository
            .getUserInfo(userRepository.getUserAuthorization())

            .doOnSubscribe {
                retry.value = null
                loading.value = true
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {
                    mUser = result.data
                    signComplete.value = result.data?.is_email_verified

                    phoneComplete.value = result.data?.is_phone_number_verified

//                    peymentComplete.value = result.data?.is_payment_info_verified
//                    identityComplete.value = result.data?.is_identity_info_verified

                    imageComplete.value = !result.data?.avatar.isNullOrEmpty()


                    smartIDComplete.value = !result.data?.smart_id_personal_code.isNullOrEmpty()

                } else {
                    retry.value = result.error?.message
                }
            }


    }


    fun onRetry() {
        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun getAvatar(): String? {
        return mUser?.avatar

    }


}