package com.ronaker.app.ui.profileNameEdit


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileNameEditViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()

    val userLastName: MutableLiveData<String> = MutableLiveData()
    val userFirstName: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null

    private var mUser: User? = null


    init {
        userRepository.getUserInfo()?.apply {
            fillUser(this)
        }
    }

    fun loadData() {


    }

    fun saveInfo(firstName: String?, lastName: String?) {

        val user = User()
        user.first_name = firstName
        user.last_name = lastName

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

        userFirstName.value = user.first_name ?: ""
        userLastName.value = user.last_name ?: ""


    }


    fun onRetry() {
        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}