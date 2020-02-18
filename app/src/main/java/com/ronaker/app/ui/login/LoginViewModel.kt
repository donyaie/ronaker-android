package com.ronaker.app.ui.login

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.utils.AnalyticsManager
import com.ronaker.app.utils.actionLogin
import com.ronaker.app.utils.actionSignUp
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginViewModel(app: Application) : BaseViewModel(app) {

//    private val TAG = LoginViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository
    private var signinSubscription: Disposable? = null
    private var signUpSubscription: Disposable? = null
    private  var emailVerificationSubscription:Disposable?=null

    @Inject
    lateinit var context: Context

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val gotoSignUp: MutableLiveData<Boolean> = MutableLiveData()
    val gotoSignIn: MutableLiveData<Boolean> = MutableLiveData()

    enum class LoginActionEnum {
        login,
        register
    }


    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()


    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    private var userInfo: User = User()


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
                for (stateEnum in values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }

    fun onClickLoginEmail(email: String?, isValid: Boolean, inviteCode: String) {


        if (isValid) {
            userInfo.email = email
            userInfo.promotionCode = if (inviteCode.isNotBlank()) inviteCode else null

            viewState.value = LoginStateEnum.info
        }

    }

    fun onClickLoginName(
        name: String?,
        nameIsValid: Boolean,
        lastName: String?,
        lastNameValid: Boolean
    ) {

        if (nameIsValid && lastNameValid) {
            userInfo.first_name = name
            userInfo.last_name = lastName
            viewState.value = LoginStateEnum.password
        }
    }

    fun onClickLoginPassword(password: String, isValid: Boolean, repeat: String) {

        if (isValid) {
            if (password.compareTo(repeat) == 0) {

                userInfo.password = password
                signUp()
            } else {
                errorMessage.value = context.getString(R.string.text_repeated_password_not_match)
            }
        }

    }

    fun onClickLoginSignIn(
        email: String?,
        emailIsValid: Boolean,
        password: String?,
        passwordValid: Boolean
    ) {
        if (passwordValid && emailIsValid) {

            userInfo.password = password
            userInfo.email = email

            signin()
        }
    }


    fun sendVerificationEmail(){
        emailVerificationSubscription =
            userRepository.sendEmailVerification(userRepository.getUserToken()).doOnSubscribe { loadingButton.value = true }
                .doOnTerminate { loadingButton.value = false }
                .subscribe {
                    loading.value = false
                    goNext.value = true

                }
    }


    private fun signin() {
        signinSubscription =
            userRepository.loginUser(userInfo).doOnSubscribe { loadingButton.value = true }
                .doOnTerminate { loadingButton.value = false }
                .subscribe { result ->
                    loading.value = false
                    if (result.isSuccess()) {



                        getAnalytics()?.actionLogin(AnalyticsManager.Param.LOGIN_METHOD_NORMAL)
                        goNext.value = true

                    } else {
                        errorMessage.value = result.error?.message
                    }
                }

    }

    fun onClickGotoSignUp() {
        gotoSignUp.value = true
    }

    fun onClickGotoSignIn() {

        gotoSignIn.value = true
    }


    private fun signUp() {
        signUpSubscription =
            userRepository.registerUser(userInfo).doOnSubscribe { loadingButton.value = true }
                .doOnTerminate { loadingButton.value = false }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        getAnalytics()?.actionSignUp(AnalyticsManager.Param.LOGIN_METHOD_NORMAL)


                         sendVerificationEmail()
                    } else {
                        errorMessage.value = result.error?.message
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


    override fun onCleared() {
        super.onCleared()
        signUpSubscription?.dispose()
        signinSubscription?.dispose()
    }

}