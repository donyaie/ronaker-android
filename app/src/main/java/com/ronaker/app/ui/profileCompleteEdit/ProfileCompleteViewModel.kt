package com.ronaker.app.ui.profileCompleteEdit

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class ProfileCompleteViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {



    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val phoneComplete: MutableLiveData<Boolean> = MutableLiveData()
    val imageComplete: MutableLiveData<Boolean> = MutableLiveData()
    val stripeComplete: MutableLiveData<Boolean> = MutableLiveData()
    val signComplete: MutableLiveData<Boolean> = MutableLiveData()
    val peymentComplete: MutableLiveData<Boolean> = MutableLiveData()
    val identityComplete: MutableLiveData<Boolean> = MutableLiveData()
    val smartIDComplete: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    private var mUser: User? = null


    init {

        userRepository.getUserInfo()?.let { fillView(it) }
    }

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
                    result.data?.let { fillView(it) }
                } else {
                    retry.value = result.error?.message
                }
            }


    }


    fun fillView(user: User){
        mUser = user
        signComplete.value = user.is_email_verified

        phoneComplete.value = user.is_phone_number_verified

//                    peymentComplete.value = result.data?.is_payment_info_verified
//                    identityComplete.value = result.data?.is_identity_info_verified

        imageComplete.value = !user.avatar.isNullOrEmpty()


        smartIDComplete.value = !user.smart_id_personal_code.isNullOrEmpty()

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