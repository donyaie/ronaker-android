package com.ronaker.app.ui.dashboard

import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class DashboardViewModel: BaseViewModel(){

    private  var subscription: Disposable?=null

    init{
    }




    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

}