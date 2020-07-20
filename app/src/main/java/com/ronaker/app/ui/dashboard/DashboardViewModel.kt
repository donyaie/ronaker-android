package com.ronaker.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.AnalyticsManager
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DashboardViewModel(app: Application) : BaseViewModel(app) {

    private var subscription: Disposable? = null


    @Inject
    lateinit
    var userRepository: UserRepository

    val goLogin: MutableLiveData<Boolean> = MutableLiveData()

    val goEmail: MutableLiveData<Boolean> = MutableLiveData()

    var islogin = false

    init {
        if (!userRepository.isLogin()) {
            islogin = false
            goLogin.value = true
        } else {
            islogin = true
            userRepository.getUserInfo()?.let {
                it.suid?.let { suid ->
                    getAnalytics()?.setUserId(suid)
                    AnalyticsManager.setUserId(suid)
                }
                if (it.is_email_verified != true) {
                    goEmail.value = true
                }


            }


        }


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

}