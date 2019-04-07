package com.ronaker.app.data

import com.ronaker.app.base.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.UserApi
import com.ronaker.app.data.network.request.UserLoginRequestModel
import com.ronaker.app.data.network.request.UserRegisterRequestModel
import com.ronaker.app.data.network.response.UserInfoResponceModel
import com.ronaker.app.data.network.response.UserRegisterResponseModel
import com.ronaker.app.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(private val userApi: UserApi, private val preferencesProvider: PreferencesProvider) {


    private val TokenKey = "tokenKey"

    fun registerUser(user: User): Observable<Result<UserRegisterResponseModel>> {
        val info = UserRegisterRequestModel(user.email!!, user.password!!, user.first_name!!, user.last_name!!)
        return userApi.registerUser(info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun loginUser(user: User): Observable<Result<UserRegisterResponseModel>> {
        val info = UserLoginRequestModel(user.email!!, user.password!!)
        return userApi.loginUser(info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun getUserInfo(user_token: String): Observable<Result<UserInfoResponceModel>> {
        return userApi.getUserInfo(user_token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun saveUserToken(token: String) {
        preferencesProvider.putString(TokenKey, token)
    }

    fun clearLogin() {
        preferencesProvider.clearAll()
    }

    fun getUserToken(): String? {
        return preferencesProvider.getString(TokenKey, null)
    }

    fun isLogin(): Boolean {
        val token = getUserToken()
        return token != null && token.trim().isNotEmpty()
    }

}

