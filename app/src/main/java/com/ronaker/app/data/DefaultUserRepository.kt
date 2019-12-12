package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.local.PreferencesDataSource
import com.ronaker.app.data.network.UserApi
import com.ronaker.app.data.network.request.*
import com.ronaker.app.model.User
import com.ronaker.app.model.toUserModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultUserRepository(
    private val userApi: UserApi,
    private val preferencesProvider: PreferencesDataSource
) :
    UserRepository {


    private val TokenKey = "tokenKey"

    private val UserInfoKey = "userInfoKey"

    override fun registerUser(user: User): Observable<Result<User>> {
        val info = UserRegisterRequestModel(
            user.email,
            user.password,
            user.first_name,
            user.last_name
        )
        return userApi.registerUser(info)
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
        return userApi.loginUser(info)
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


    override fun updateUserInfo(user_token: String?, user: User): Observable<Result<User>> {
        val info = UserUpdateRequestModel(
            user.first_name,
            user.last_name,
            user.email,
            user.avatar
        )

        return userApi.updateUserInfo("Token $user_token", info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                it.toUserModel()
            }
            .toResult()

    }


    override fun getUserInfo(user_token: String?): Observable<Result<User>> {
        return userApi.getUserInfo("Token $user_token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {

                it.toUserModel().apply {

                    saveUserInfo(this)

                }

            }

            .toResult()

    }

    override fun addUserPhoneNumber(
        user_token: String?,
        phone_number: String
    ): Observable<Result<String>> {

        val phone =
            UserAddPhoneRequestModel(
                phone_number
            )
        return userApi.addUserPhoneNumber("Token $user_token", phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.phone_number
            }
            .toResult()

    }

    override fun activeUserPhoneNumber(
        user_token: String?,
        phone_number: String,
        code: String
    ): Observable<Result<String>> {

        val phone =
            UserActivePhoneRequestModel(
                phone_number,
                code
            )
        return userApi.activeUserPhoneNumber("Token $user_token", phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.phone_number
            }

            .toResult()

    }


    override fun addDocument(
        userToken: String?,
        imageSuid: String,
        documentType: UserRepository.DocumentTypeEnum
    ): Observable<Result<Boolean>> {

        val phone =
            UserIdentifyRequestModel(
                imageSuid,
                documentType.key
            )
        return userApi.addDocument("Token $userToken", phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()

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

    override fun getUserToken(): String? {
        return preferencesProvider.getString(TokenKey, null)
    }

    override fun isLogin(): Boolean {
        val token = getUserToken()
        return token != null && token.trim().isNotEmpty()
    }

}