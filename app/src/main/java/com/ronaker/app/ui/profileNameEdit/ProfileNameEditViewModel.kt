package com.ronaker.app.ui.profileNameEdit


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import io.reactivex.disposables.Disposable

class ProfileNameEditViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
)  : BaseViewModel() {




    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()

    val userLastName: MutableLiveData<String> = MutableLiveData()
    val userFirstName: MutableLiveData<String> = MutableLiveData()


    private var subscription: Disposable? = null

    private var mUser: User? = null


    init {

    }


    fun loadData(){
        userRepository.getUserInfo()?.let {
            fillUser(it)
        }
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

        userFirstName.postValue(user.first_name ?: "")
        userLastName.postValue(user.last_name ?: "")


    }



    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}