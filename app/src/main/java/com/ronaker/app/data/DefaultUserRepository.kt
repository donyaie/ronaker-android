package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.local.PreferencesDataSource
import com.ronaker.app.data.network.UserApi
import com.ronaker.app.data.network.request.*
import com.ronaker.app.data.network.response.StripeSetupResponseModel
import com.ronaker.app.data.network.response.UserGoogleLoginResponseModel
import com.ronaker.app.model.DocumentTypeEnum
import com.ronaker.app.model.User
import com.ronaker.app.model.toUserModel
import com.ronaker.app.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val userApi: UserApi,
    private val preferencesProvider: PreferencesDataSource
) :
    UserRepository {

    companion object {

        val USER_LANGUAGE_KEY = "Locale.Helper.Selected.Language"


    }

    private val TokenKey = "tokenKey"
    private val SkipKey = "skipVersion"
    val USER_LANGUAGE_DEFAULT = LANGUAGE_DEFAULT

    private val UserInfoKey = "userInfoKey"

    override fun registerUser(user: User): Observable<Result<User>> {
        val info = UserRegisterRequestModel(
            user.email,
            user.password,
            user.first_name,
            user.last_name,
            user.promotionCode
        )
        return userApi.registerUser(getUserLanguage(), info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.user.toUserModel().apply {
                    accessToken = it.token
                    saveUserInfo(this)
                    saveUserToken(it.token)
                }
            }
            .toResult()

    }


    override fun loginUser(user: User): Observable<Result<User>> {
        val info = UserLoginRequestModel(
            user.email,
            user.password
        )
        return userApi.loginUser(getUserLanguage(), info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                it.user.toUserModel().apply {


                    this.accessToken = it.token

                    saveUserToken(it.token)
                    saveUserInfo(this)
                }
            }

            .toResult()

    }


    override fun loginUserWithGoogle(token: String): Observable<Result<User>> {
        val info = UserGoogleLoginResponseModel(
            token
        )
        return userApi.loginGoogle(getUserLanguage(), info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.user.toUserModel().apply {
                    this.accessToken = it.token

                    saveUserToken(it.token)
                    saveUserInfo(this)
                }
            }

            .toResult()

    }


    override fun updateUserInfo(user: User): Observable<Result<User>> {
        val info = UserUpdateRequestModel(
            user.first_name,
            user.last_name,
            user.email,
            user.avatar
        )

        return userApi.updateUserInfo(getUserAuthorization(), getUserLanguage(), info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                it.toUserModel()
            }
            .toResult()

    }


    override fun getUserInfo(user_auth: String): Observable<Result<User>> {
        return userApi.getUserInfo(getUserLanguage(), user_auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {

                it.toUserModel().apply {

                    saveUserInfo(this)

                }

            }

            .toResult()

    }

    override fun docusignAuth(): Observable<Result<String>> {
        return userApi.docusignAuth(getUserAuthorization(),getUserLanguage())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {

                it.url

            }

            .toResult()

    }


    override fun sendEmailVerification(): Observable<Result<Boolean>> {
        return userApi.sendEmailVerification(getUserAuthorization(), getUserLanguage())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {

                true

            }

            .toResult()

    }

    override fun addUserPhoneNumber(
        phone_number: String
    ): Observable<Result<String>> {

        val phone =
            UserAddPhoneRequestModel(
                phone_number
            )
        return userApi.addUserPhoneNumber(getUserAuthorization(), getUserLanguage(), phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.phone_number
            }
            .toResult()

    }

    override fun activeUserPhoneNumber(
        phone_number: String,
        code: String
    ): Observable<Result<String>> {

        val phone =
            UserActivePhoneRequestModel(
                phone_number,
                code
            )
        return userApi.activeUserPhoneNumber(getUserAuthorization(), getUserLanguage(), phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.phone_number
            }

            .toResult()

    }


    override fun getSmartIDVerificationCode(

        national_code: String,
        personal_code: String
    ): Observable<Result<String>> {

        val request =
            UserSmartIdVerificationCodeRequestModel(
                national_code,
                personal_code
            )
        return userApi.getSmartIDVerificationCode(
            getUserAuthorization(),
            getUserLanguage(),
            request
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.verification_code
            }
            .toResult()

    }


    override fun startSmartIDAuth(

        national_code: String,
        personal_code: String
    ): Observable<Result<Boolean>> {

        val request =
            UserSmartIdVerificationCodeRequestModel(
                national_code,
                personal_code
            )
        return userApi.startSmartIDAuth(getUserAuthorization(), getUserLanguage(), request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }

    override fun stripeSetup(): Observable<Result<StripeSetupResponseModel>> {

        return userApi.stripeSetup(getUserLanguage(), getUserAuthorization())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toResult()
    }


    override fun forgetPassword(
        email: String
    ): Observable<Result<Boolean>> {
        val request = UserForgetPasswordRequestModel(email)

        return userApi.forgetPassword(getUserLanguage(), request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }

    override fun forgetPasswordConfirm(
        token: String,
        password: String
    ): Observable<Result<Boolean>> {
        val request = UserForgetPasswordConfirmRequestModel(token, password)

        return userApi.forgetPasswordConfirm(getUserLanguage(), request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


    override fun checkSmartIDSession(
    ): Observable<Result<Boolean>> {
        return userApi.checkSmartIDSession(getUserAuthorization(), getUserLanguage())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


    override fun addDocument(
        imageSuid: String,
        documentType: DocumentTypeEnum
    ): Observable<Result<Boolean>> {

        val phone =
            UserIdentifyRequestModel(
                imageSuid,
                documentType.key
            )
        return userApi.addDocument(getUserAuthorization(), getUserLanguage(), phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

    }


    override fun saveUpdateSkipVersion(updateVersion: Int) {
        preferencesProvider.putInt(SkipKey, updateVersion)
    }

    override fun getUpdateSkipVersion(): Int {
        return preferencesProvider.getInt(SkipKey, 0)
    }

    override fun saveUserInfo(info: User?) {
        preferencesProvider.putObject(UserInfoKey, info)
    }

    override fun getUserInfo(): User? {

        return preferencesProvider.getObject(UserInfoKey, User::class.java)
    }

    override fun saveUserToken(token: String?) {
        preferencesProvider.putString(TokenKey, token)
    }

    override fun clearLogin() {
        preferencesProvider.clearAll()
    }

    override fun getUserAuthorization(): String {
        return "Token ${getUserToken()}"
    }


    override fun getUserLanguage(): String {

        return if (LocaleHelper.getCurrentLanguage().isEmpty()) {
            preferencesProvider.getString(
                USER_LANGUAGE_KEY,
                USER_LANGUAGE_DEFAULT
            ) ?: USER_LANGUAGE_DEFAULT
        } else LocaleHelper.getCurrentLanguage()

    }

    override fun getPrivacyUrl(): String {

        getUserLanguage().let {

            return if (it.toLowerCase(Locale.getDefault()).compareTo("lt") == 0) {
                PRIVACY_URL_LT
            } else {
                PRIVACY_URL
            }

        }
    }

    override fun getTermUrl(): String {

        getUserLanguage().let {

            return if (it.toLowerCase(Locale.getDefault()).compareTo("lt") == 0) {
                TERM_URL_LT
            } else {
                TERM_URL
            }

        }
    }


    override fun getUserToken(): String? {
        return preferencesProvider.getString(TokenKey, null)
    }

    override fun isLogin(): Boolean {
        val token = getUserToken()
        return !token.isNullOrBlank()
    }

}