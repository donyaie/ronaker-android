package com.ronaker.app.ui.profile


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.nameFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
)  : BaseViewModel() {



    val errorMessage: MutableLiveData<String> = MutableLiveData()


    val userAvatar: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val editVisibility: MutableLiveData<Int> = MutableLiveData()
    val completeProgress: MutableLiveData<Int> = MutableLiveData()
    val completeProgressVisibility: MutableLiveData<Int> = MutableLiveData()
    val completeVisibility: MutableLiveData<Int> = MutableLiveData()
    val userStep: MutableLiveData<String> = MutableLiveData()


    val logOutAction: MutableLiveData<Boolean> = MutableLiveData()
    val userProfileComplete: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null


     var isComplete=false

    init {


        userRepository.getUserInfo()?.let { fillUser(it) }

    }

    fun updateUser() {
        uiScope.launch {
            getUserData()
        }
    }

    private suspend fun getUserData() =
        withContext(Dispatchers.IO) {
            subscription = userRepository
                .getUserInfo(userRepository.getUserAuthorization())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                }
                .doOnTerminate {
                }

                .subscribe { result ->
                    if (result.isSuccess()) {
                        result.data?.let {
                            userRepository.saveUserInfo(it)
                            fillUser(it)
                        }

                    } else {

//                        if(result?.error?.responseCode==401){
//                            logOut()
//                        }

                        errorMessage.postValue(result.error?.message)
                    }

                }


        }



    private fun fillUser(user: User) {


        if (user.first_name.isNullOrEmpty()) {
            userName.postValue("${user.email}")
        } else
            userName.postValue(nameFormat(user.first_name,user.last_name)+"  ")


        user.avatar?.let {
            userAvatar.postValue(BASE_URL + it)
        }


        if (user.isComplete()) {
            completeProgressVisibility.postValue(View.GONE)
            completeVisibility.postValue(View.GONE)
            editVisibility.postValue(View.VISIBLE)

            isComplete=true

            userProfileComplete.postValue(true)
        } else {
            isComplete=false

            completeProgressVisibility.postValue(View.VISIBLE)
            completeProgress.postValue(user.completed)
            userStep.postValue(user.completed.toString())

            completeVisibility.postValue(View.VISIBLE)
            editVisibility.postValue(View.GONE)
            userProfileComplete.postValue(false)
        }





    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}