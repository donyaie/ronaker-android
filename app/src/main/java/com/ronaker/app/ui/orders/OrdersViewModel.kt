package com.ronaker.app.ui.orders


import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class OrdersViewModel : BaseViewModel() {




    private var subscription: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }




}