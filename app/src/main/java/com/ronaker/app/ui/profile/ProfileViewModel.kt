package com.ronaker.app.ui.profile


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.model.toUser
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileViewModel: BaseViewModel(){


    @Inject
    lateinit
    var userRepository: UserRepository



    val errorMessage:MutableLiveData<String> = MutableLiveData()
    val loading:MutableLiveData<Boolean> = MutableLiveData()


    val userAvatar:MutableLiveData<String> = MutableLiveData()
    val userName:MutableLiveData<String> = MutableLiveData()
    val editVisibility:MutableLiveData<Int> = MutableLiveData()
    val completeProgress:MutableLiveData<Int> = MutableLiveData()
    val completeProgressVisibility:MutableLiveData<Int> = MutableLiveData()
    val completeVisibility:MutableLiveData<Int> = MutableLiveData()
    val userStep:MutableLiveData<String> = MutableLiveData()



    val logOutAction:MutableLiveData<Boolean> = MutableLiveData()




    private  var subscription: Disposable?=null

    init{


        userRepository.getUserInfo()?.let { fillUser(it) }

    }

    fun updateUser(){
        subscription = userRepository
            .getUserInfo(userRepository.getUserToken())

            .doOnSubscribe {
            }
            .doOnTerminate {
            }

            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.toUser()?.let {


                        userRepository.saveUserInfo(it)
                        fillUser(it)


                    }



                } else {

                }
            }


    }


    fun fillUser(user: User){

        var complete=0

        userName.value="${user.first_name} ${user.last_name}"

        user.avatar?.let {
            userAvatar.value= BASE_URL+it
            complete++
        }

        user.is_email_verified?.let { if(it) complete++ }

        user.is_phone_number_verified?.let { if(it) complete++ }





        if(complete==5){
            completeProgressVisibility.value= View.GONE
            completeVisibility.value=View.GONE
            editVisibility.value=View.VISIBLE
        }
        else{

            completeProgressVisibility.value= View.VISIBLE
            completeProgress.value=complete
            userStep.value= "$complete of 5"

            completeVisibility.value=View.VISIBLE
            editVisibility.value=View.GONE
        }


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