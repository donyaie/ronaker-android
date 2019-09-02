package com.ronaker.app.ui.inbox


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class InboxViewModel: BaseViewModel(){


    @Inject
    lateinit
    var userRepository: UserRepository



    val errorMessage:MutableLiveData<String> = MutableLiveData()
    val loading:MutableLiveData<Boolean> = MutableLiveData()



    private  var subscription: Disposable?=null

    init{

    }




    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}