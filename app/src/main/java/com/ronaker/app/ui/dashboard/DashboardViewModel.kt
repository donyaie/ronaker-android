package com.ronaker.app.ui.dashboard

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.AnalyticsManager
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DashboardViewModel @ViewModelInject constructor(
    userRepository: UserRepository,analytics: FirebaseAnalytics
)  : BaseViewModel() {

    private var subscription: Disposable? = null


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
                    analytics.setUserId(suid)
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