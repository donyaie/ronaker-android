package com.ronaker.app.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.AnalyticsManager
import io.reactivex.disposables.Disposable

class DashboardViewModel @ViewModelInject constructor(
    val userRepository: UserRepository, val analytics: FirebaseAnalytics
) : BaseViewModel() {

    private var subscription: Disposable? = null


    val goLogin: MutableLiveData<Boolean> = MutableLiveData()

    val goEmail: MutableLiveData<Boolean> = MutableLiveData()


    init {
//        islogin ()


    }

    var islogin = false

    fun checklogin(): Boolean {

        islogin = false

        if (!userRepository.isLogin()) {
            goLogin.value = true
            islogin = false
        } else {

            userRepository.getUserInfo()?.let {
                it.suid?.let { suid ->
                    analytics.setUserId(suid)
                    AnalyticsManager.setUserId(suid)
                }
                if (!it.is_email_verified) {
                    goEmail.value = true
                }


            }


            islogin = true


        }
        return islogin
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun isSkipVersion(availableVersionCode: Int): Boolean {

        if (availableVersionCode != 0 && availableVersionCode == userRepository.getUpdateSkipVersion())
            return true
        return false
    }

    fun setSkipVersion(availableVersionCode: Int) {

        userRepository.saveUpdateSkipVersion(availableVersionCode)

    }

}