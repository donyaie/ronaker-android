package com.ronaker.app.ui.profile


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileViewModel: BaseViewModel(){


    @Inject
    lateinit
    var userRepository: UserRepository



    val errorMessage:MutableLiveData<String> = MutableLiveData()
    val loading:MutableLiveData<Boolean> = MutableLiveData()

    val logOutAction:MutableLiveData<Boolean> = MutableLiveData()



    private  var subscription: Disposable?=null

    init{

    }


   fun onClickLogout(){
       userRepository.clearLogin()
       logOutAction.value=true

   }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}