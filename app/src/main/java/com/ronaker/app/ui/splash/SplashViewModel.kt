package com.ronaker.app.ui.splash

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule
class SplashViewModel: BaseViewModel(){

    private lateinit var subscription: Disposable

    val goLogin: MutableLiveData<Boolean> = MutableLiveData()
    val goDashboard: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var userRepository: UserRepository




    init{

            goLogin.value= !userRepository.isLogin()


    }



    override fun onCleared() {
        super.onCleared()
//        subscription?.dispose()
    }

}