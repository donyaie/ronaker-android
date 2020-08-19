package com.ronaker.app.ui.profileEmailEdit


import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileEmailEditViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
)  : BaseViewModel() {



    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()

    val userEmail: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null

    private var mUser: User? = null


    init {
        userRepository.getUserInfo()?.apply {
            fillUser(this)
        }
    }

    fun loadData() {


    }

    fun saveInfo(email: String?) {

        val user = User()
        user.email = email

        subscription?.dispose()
        subscription = userRepository
            .updateUserInfo( user)

            .doOnSubscribe {
                loadingButton.value = true
            }
            .doOnTerminate {
                loadingButton.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {

                    goNext.value = true

                } else {
                    errorMessage.value = result.error?.message
                }
            }
    }


    private fun fillUser(user: User) {

        mUser = user

        userEmail.value = user.email ?: ""


    }


    fun onRetry() {
        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}