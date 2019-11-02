package com.ronaker.app.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DashboardViewModel: BaseViewModel(){

    private  var subscription: Disposable?=null


    @Inject
    lateinit
    var userRepository: UserRepository

    val goLogin: MutableLiveData<Boolean> = MutableLiveData()

    init{
        if (!userRepository.isLogin())
            goLogin.value = true


    }




    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

}