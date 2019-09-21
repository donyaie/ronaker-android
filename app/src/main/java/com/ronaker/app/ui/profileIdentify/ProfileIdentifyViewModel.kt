package com.ronaker.app.ui.profileIdentify


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileIdentifyViewModel : BaseViewModel() {


    @Inject
    lateinit
    var userRepository: UserRepository
    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()





    private var subscription: Disposable? = null

    init {

    }


    fun loadData() {



    }



    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}