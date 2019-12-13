package com.ronaker.app.ui.profileEmailVerify


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.IntentManeger
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileEmailVerifyViewModel  (val app: Application): BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val goNex: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    fun loadData() {

        subscription = userRepository
            .getUserInfo(userRepository.getUserToken())

            .doOnSubscribe {
            }
            .doOnTerminate {
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    if(result?.data?.is_email_verified==true){
                        goNex.value=true
                    }

                } else {
                    errorMessage.value = result.error?.message
                }
            }


    }

    fun gotoMailClick(){

        IntentManeger.openMailBox(app)
    }


    fun resendMailClick(){

    }

    fun onRetry() {
//        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


    fun logout(){
        userRepository.clearLogin()
    }



}