package com.ronaker.app.ui.login

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.utils.*
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit var userRepository: UserRepository
    private var signinSubscription: Disposable? = null
    private var signUpSubscription: Disposable? = null
    private var emailVerificationSubscription: Disposable? = null


    var forgetSubscription: Disposable? = null
    val actionState: MutableLiveData<LoginActionEnum> = MutableLiveData()
    val viewState: MutableLiveData<LoginStateEnum> = MutableLiveData()


    val inviteCodeText: MutableLiveData<String> = MutableLiveData()

    val passwordLengthVisibility: MutableLiveData<Int> = MutableLiveData()
    val passwordAlphabetVisibility: MutableLiveData<Int> = MutableLiveData()
    val passwordMatchVisibility: MutableLiveData<Int> = MutableLiveData()


    @Inject
    lateinit var resourcesRepository: ResourcesRepository

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val successMessage: MutableLiveData<String> = MutableLiveData()


    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val gotoSignUp: MutableLiveData<Boolean> = MutableLiveData()
    val gotoSignIn: MutableLiveData<Boolean> = MutableLiveData()
    val keyboardDown: MutableLiveData<Boolean> = MutableLiveData()

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
//        info(2),
        password(2),
        login(3),
        forget(4);

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

            viewState.value = LoginStateEnum.password
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

        keyboardDown.postValue(true)
        if (isValid) {
            if (password.compareTo(repeat) == 0) {

                userInfo.password = password
                signUp()
            } else {
                errorMessage.value = resourcesRepository.getString(R.string.text_repeated_password_not_match)
            }
        }

    }

    fun onClickLoginSignIn(
        email: String?,
        emailIsValid: Boolean,
        password: String?,
        passwordValid: Boolean
    ) {

        keyboardDown.postValue(true)

        if (passwordValid && emailIsValid) {

            userInfo.password = password
            userInfo.email = email

            signin()
        }
    }


    fun sendVerificationEmail() {
        emailVerificationSubscription =
            userRepository.sendEmailVerification()
                .doOnSubscribe { loadingButton.postValue(true) }
                .doOnTerminate { loadingButton.postValue(false) }
                .subscribe {


                    loading.postValue(false)
                    goNext.postValue(true)

                }
    }


    private fun signin() {
        signinSubscription =
            userRepository.loginUser(userInfo).doOnSubscribe { loadingButton.postValue(true) }
                .doOnTerminate { loadingButton.postValue(false) }
                .subscribe { result ->
                    loading.postValue(false)
                    if (result.isSuccess()) {

                        AppDebug.log("Login", result?.data.toString())


                        getAnalytics()?.actionLogin(AnalyticsManager.Param.LOGIN_METHOD_NORMAL)
                        goNext.postValue(true)

                    } else {

                        if (result?.error?.responseCode == 406) {
                            errorMessage.postValue(resourcesRepository.getString(R.string.error_login_email_or_password_wrong))
                        } else
                            errorMessage.postValue(result.error?.message)
                    }
                }

    }

    fun onClickLoginResetPassword(
        email: String?,
        emailIsValid: Boolean
    ) {

        if (emailIsValid && email != null) {
            forgetSubscription =
                userRepository.forgetPassword(email).doOnSubscribe { loadingButton.postValue(true) }
                    .doOnTerminate { loadingButton.postValue(false) }
                    .subscribe { result ->
                        loading.postValue(false)
                        if (result.isSuccess()) {


                            viewState.postValue(LoginStateEnum.home)

                            successMessage.postValue(resourcesRepository.getString(R.string.text_send_activation_success))


                        } else {
                            errorMessage.postValue(result.error?.message)
                        }
                    }
        }

    }

    fun onClickGotoSignUp() {
        if (!mInviteCode.isNullOrBlank())
            inviteCodeText.postValue(mInviteCode)
        gotoSignUp.postValue(true)

    }

    fun onClickGotoForget() {

        viewState.value = LoginStateEnum.forget
    }

    fun onClickGotoSignIn() {

        gotoSignIn.postValue(true)
    }


    private fun signUp() {
        signUpSubscription =
            userRepository.registerUser(userInfo).doOnSubscribe { loadingButton.value = true }
                .doOnTerminate { loadingButton.value = false }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        getAnalytics()?.actionSignUp(AnalyticsManager.Param.LOGIN_METHOD_NORMAL)

                        AppDebug.log("Login", result?.data.toString())

                        sendVerificationEmail()
                    } else {
                        errorMessage.value = result.error?.message
                    }
                }
    }


    val loginClickListener = View.OnClickListener {


        actionState.value = LoginActionEnum.login
        viewState.value = LoginStateEnum.login
    }

    val signupClickListener = View.OnClickListener {

        actionState.value = LoginActionEnum.register
        viewState.value = LoginStateEnum.email
    }


    fun validatePassword(password: String?, repeat: String): Boolean {
        var isValid = true

        if (password.isNullOrBlank() || password.length < 8) {
            passwordLengthVisibility.postValue(View.VISIBLE)
            isValid = false
        } else
            passwordLengthVisibility.postValue(View.GONE)

        if (password?.trim()?.isNumeric() == true) {
            passwordAlphabetVisibility.postValue(View.VISIBLE)
            isValid = false
        } else
            passwordAlphabetVisibility.postValue(View.GONE)

        if (password?.compareTo(repeat) != 0) {
            passwordMatchVisibility.postValue(View.VISIBLE)
            isValid = false
        } else
            passwordMatchVisibility.postValue(View.GONE)


        AppDebug.log("validatePassword", "password: $password , repeat: $repeat isValid: $isValid")

        return isValid
    }


    override fun onCleared() {
        super.onCleared()
        signUpSubscription?.dispose()
        signinSubscription?.dispose()
    }


    var mInviteCode: String? = null

    fun setInviteCode(inviteCode: String) {

        mInviteCode = inviteCode


    }

}