package com.ronaker.app.ui.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.model.toUser
import com.ronaker.app.utils.Debug
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    internal val TAG = LoginViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository
    private var signinSubscription: Disposable? = null
    private var signUpSubscription: Disposable? = null


    val errorMessage:MutableLiveData<String> = MutableLiveData()
    val loading:MutableLiveData<Boolean> = MutableLiveData()

    enum class LoginActionEnum {
        login,
        register
    }


    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    public var userInfo: User = User()


    var emailError = MutableLiveData<Boolean?>()

    enum class LoginStateEnum constructor(position: Int) {
        home(0),
        email(1),
        info(2),
        password(3),
        login(4);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): LoginStateEnum {
                var state = home
                for (stateEnum in LoginStateEnum.values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }

    fun onClickLoginEmail(email: String?, isValid: Boolean) {


        if (isValid) {
            userInfo.email = email
            viewState.value = LoginStateEnum.info
        }

    }

    fun onClickLoginName(name: String?, nameIsValid: Boolean, lastName: String?, lastNameVaalid: Boolean) {

        if (nameIsValid && lastNameVaalid) {
            userInfo.first_name = name
            userInfo.last_name = lastName
            viewState.value = LoginStateEnum.password
        }
    }

    fun onClickLoginPassword(password: String?, isValid: Boolean) {

        if (isValid) {
            userInfo.password = password
            signUp()
        }

    }

    fun onClickLoginSignIn(email: String?, emailIsValid: Boolean, password: String?, passwordValid: Boolean) {
        if (passwordValid && emailIsValid) {

            userInfo.password = password
            userInfo.email = email

            signin()
        }
    }


    fun signin() {
        signinSubscription = userRepository.loginUser(userInfo).doOnSubscribe { loading.value=true}
            .doOnTerminate {loading.value=false}
            .subscribe { result ->
                loading.value=false
                if (result.isSuccess()) {

                    userRepository.saveUserToken(result.data?.token)
                    result.data?.user?.let { userRepository.saveUserInfo(it.toUser()) }


                    goNext.value=true

                } else {
                    errorMessage.value= result.error?.detail
                }
            }

    }



    fun signUp() {
        signUpSubscription = userRepository.registerUser(userInfo).doOnSubscribe {loading.value=true }
            .doOnTerminate {loading.value=false}
            .subscribe { result ->
                loading.value=false
                if (result.isSuccess()) {

                    userRepository.saveUserToken(result.data?.token)
                    result.data?.user?.let { userRepository.saveUserInfo(it.toUser()) }
                    goNext.value=true
                } else {
                   errorMessage.value= result.error?.detail
                }
            }
    }


    val actionState: MutableLiveData<LoginActionEnum> = MutableLiveData()
    val viewState: MutableLiveData<LoginStateEnum> = MutableLiveData()

    val loginClickListener = View.OnClickListener {


        actionState.value = LoginActionEnum.login
        viewState.value = LoginStateEnum.login
    }

    val signupClickListener = View.OnClickListener {

        actionState.value = LoginActionEnum.register
        viewState.value = LoginStateEnum.email
    }


    init {

    }


    override fun onCleared() {
        super.onCleared()
        signUpSubscription?.dispose()
        signinSubscription?.dispose()
    }

}