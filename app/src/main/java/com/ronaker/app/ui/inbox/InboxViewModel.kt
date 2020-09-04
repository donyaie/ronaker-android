package com.ronaker.app.ui.inbox


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class InboxViewModel : BaseViewModel() {



    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    private var subscription: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}