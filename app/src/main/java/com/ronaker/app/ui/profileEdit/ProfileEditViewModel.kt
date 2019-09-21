package com.ronaker.app.ui.profileEdit


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileEditViewModel : BaseViewModel() {


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val phoneComplete: MutableLiveData<Boolean> = MutableLiveData()
    val imageComplete: MutableLiveData<Boolean> = MutableLiveData()
    val signComplete: MutableLiveData<Boolean> = MutableLiveData()
    val peymentComplete: MutableLiveData<Boolean> = MutableLiveData()
    val identityComplete: MutableLiveData<Boolean> = MutableLiveData()



    private var subscription: Disposable? = null

    init {

    }


    fun loadData() {

        subscription = userRepository
            .getUserInfo(userRepository.getUserToken())

            .doOnSubscribe {
                retry.value = false
                loading.value = true
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    signComplete.value = result.data?.is_email_verified

                    phoneComplete.value = result.data?.is_phone_number_verified

                    peymentComplete.value = result.data?.is_payment_info_verified
                    identityComplete.value = result.data?.is_identity_info_verified

                    imageComplete.value = result.data?.avatar != null


                } else {
                    retry.value = true
                    errorMessage.value = result.error?.detail
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


}