package com.ronaker.app.ui.search


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class SearchViewModel @ViewModelInject constructor() : BaseViewModel() {



    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val productPrice: MutableLiveData<String> = MutableLiveData()

    val productPriceTitle: MutableLiveData<String> = MutableLiveData()


    lateinit var suid: String


    private var subscription: Disposable? = null


    fun loadProduct(suid: String) {

        this.suid = suid


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}