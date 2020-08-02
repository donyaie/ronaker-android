package com.ronaker.app.ui.profileEdit


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileEditViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository

    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val userNumber: MutableLiveData<String> = MutableLiveData()
    val userEmail: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()


    val userAvatar: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null

    private var mUser: User? = null


    init {
        userRepository.getUserInfo()?.apply {
            fillUser(this)
        }
    }

    fun loadData() {
        subscription?.dispose()
        subscription = userRepository
            .getUserInfo(userRepository.getUserAuthorization())

            .doOnSubscribe {
//                loading.value = true
            }
            .doOnTerminate {
//                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {
                    result.data?.apply {
                        fillUser(this)
                    }


                } else {
                    errorMessage.value = result.error?.message
                }
            }


    }


    fun fillUser(user: User) {

        mUser = user


        user.avatar?.let {
            userAvatar.value = BASE_URL + it
        }

        userName.value = (user.first_name ?: "") + " " + (user.last_name ?: "")


        userNumber.value = user.phone_number ?: ""

        userEmail.value = user.email ?: ""


    }


    fun onRetry() {
        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun getAvatar(): String? {
        return mUser?.avatar

    }


}