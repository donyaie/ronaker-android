package com.ronaker.app.ui.dashboard

import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class DashboardViewModel: BaseViewModel(){

    private lateinit var subscription: Disposable

    init{
    }




    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

}