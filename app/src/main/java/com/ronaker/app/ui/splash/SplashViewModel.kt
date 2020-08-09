package com.ronaker.app.ui.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import javax.inject.Inject

class SplashViewModel(app: Application) : BaseViewModel(app) {


    val goLogin: MutableLiveData<Boolean> = MutableLiveData()
    val goDashboard: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var userRepository: UserRepository


    init {
        if (userRepository.isLogin())
            goDashboard.value = true
        else
            goLogin.value = true
    }


}