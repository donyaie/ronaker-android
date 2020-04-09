package com.ronaker.app.ui.profile


import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()


    val userAvatar: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val editVisibility: MutableLiveData<Int> = MutableLiveData()
    val completeProgress: MutableLiveData<Int> = MutableLiveData()
    val completeProgressVisibility: MutableLiveData<Int> = MutableLiveData()
    val completeVisibility: MutableLiveData<Int> = MutableLiveData()
    val userStep: MutableLiveData<String> = MutableLiveData()


    val logOutAction: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    init {


        userRepository.getUserInfo()?.let { fillUser(it) }

    }
    fun updateUser() {
        uiScope.launch {
            getUserData()
        }
    }

    private suspend  fun getUserData()  =
        withContext(Dispatchers.Default){
        subscription = userRepository
            .getUserInfo(userRepository.getUserToken())
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
                    errorMessage.postValue(result.error?.message)
                }

            }


    }


    private fun fillUser(user: User) {

        var complete = 0

        userName.postValue( "${user.first_name} ${user.last_name}")

        user.avatar?.let {
            userAvatar.postValue( BASE_URL + it)
            complete++
        }

        user.is_email_verified?.let { if (it) complete++ }

        user.is_phone_number_verified?.let { if (it) complete++ }
        user.is_payment_info_verified?.let { if (it) complete++ }
        user.is_identity_info_verified?.let { if (it) complete++ }


        if (complete == 5) {
            completeProgressVisibility.postValue( View.GONE)
            completeVisibility.postValue( View.GONE)
            editVisibility.postValue( View.VISIBLE)
        } else {

            completeProgressVisibility.postValue( View.VISIBLE)
            completeProgress.postValue( complete)
            userStep.postValue( complete.toString())

            completeVisibility.postValue( View.VISIBLE)
            editVisibility.postValue( View.GONE)
        }


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}