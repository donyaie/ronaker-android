package com.ronaker.app.ui.login

import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class LoginViewModel: BaseViewModel(){

    private lateinit var subscription: Disposable

    init{


    }



    override fun onCleared() {
        super.onCleared()
//        subscription?.dispose()
    }

}