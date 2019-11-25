package com.ronaker.app.ui.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class SplashViewModel (app: Application): BaseViewModel(app) {

    private lateinit var subscription: Disposable

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


    override fun onCleared() {
        super.onCleared()
//        subscription?.dispose()
    }

}