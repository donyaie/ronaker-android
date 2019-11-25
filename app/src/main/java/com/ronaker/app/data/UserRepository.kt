package com.ronaker.app.data

import com.ronaker.app.data.local.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.local.PreferencesDataSource
import com.ronaker.app.data.network.UserApi
import com.ronaker.app.data.network.request.*
import com.ronaker.app.data.network.response.FreeResponseModel
import com.ronaker.app.data.network.response.UserAddPhoneResponceModel
import com.ronaker.app.data.network.response.UserInfoResponceModel
import com.ronaker.app.data.network.response.UserRegisterResponseModel
import com.ronaker.app.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(private val userApi: UserApi, private val preferencesProvider: PreferencesDataSource) {


    enum class DocumentTypeEnum constructor(key: String) {
        IdCard("id_card"),
        Passport("passport"),
        DrivingLicense("driving_license"),
        None("");


        var key: String = ""
            internal set

        init {
            this.key = key
        }

        companion object {
            operator fun get(position: String): DocumentTypeEnum {
                var state = None
                for (stateEnum in DocumentTypeEnum.values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }

    private val TokenKey = "tokenKey"

    private val UserInfoKey = "userInfoKey"

    fun registerUser(user: User): Observable<Result<UserRegisterResponseModel>> {
        val info = UserRegisterRequestModel(user.email, user.password, user.first_name, user.last_name)
        return userApi.registerUser(info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

//            .map {
//                it.results.map {
//                    Movie(it.title, it.overview, it.backdrop_path)
//                }
//            }

            .toResult()

    }


    fun loginUser(user: User): Observable<Result<UserRegisterResponseModel>> {
        val info = UserLoginRequestModel(user.email, user.password)
        return userApi.loginUser(info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun updateUserInfo(user_token: String?,user: User): Observable<Result<UserInfoResponceModel>> {
        var info=UserUpdateRequestModel(user.first_name,user.last_name,user.email,user.avatar)

        return userApi.updateUserInfo("Token $user_token",info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun getUserInfo(user_token: String?): Observable<Result<UserInfoResponceModel>> {
        return userApi.getUserInfo("Token $user_token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun addUserPhoneNumber(user_token: String?, phone_number: String): Observable<Result<UserAddPhoneResponceModel>> {

        var phone = UserAddPhoneRequestModel(phone_number)
        return userApi.addUserPhoneNumber("Token $user_token", phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun activeUserPhoneNumber(
        user_token: String?,
        phone_number: String,
        code: String
    ): Observable<Result<UserRegisterResponseModel>> {

        var phone = UserActivePhoneRequestModel(phone_number, code)
        return userApi.activeUserPhoneNumber("Token $user_token", phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun addDocument(
        userToken: String?,
        imageSuid: String,
        documentType: DocumentTypeEnum
    ): Observable<Result<FreeResponseModel>> {

        var phone = UserIdentifyRequestModel(imageSuid, documentType.key)
        return userApi.addDocument("Token $userToken",phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


    fun saveUserInfo(info:User?) {
        preferencesProvider.putObject(UserInfoKey,info)
    }
    fun getUserInfo():User? {
        return preferencesProvider.getObject<User?>(UserInfoKey, User::class.java)
    }

    fun saveUserToken(token: String?) {
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

