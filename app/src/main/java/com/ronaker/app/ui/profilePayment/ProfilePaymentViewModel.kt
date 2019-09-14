package com.ronaker.app.ui.profilePayment


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfilePaymentViewModel : BaseViewModel() {


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()





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