package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.FreeResponseModel
import com.ronaker.app.data.network.response.UserAddPhoneResponceModel
import com.ronaker.app.data.network.response.UserInfoResponceModel
import com.ronaker.app.data.network.response.UserRegisterResponseModel
import com.ronaker.app.model.User
import io.reactivex.Observable

interface  UserRepository {
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
            operator fun get(position: String): UserRepository.DocumentTypeEnum {
                var state = None
                for (stateEnum in DocumentTypeEnum.values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }

    fun registerUser(user: User): Observable<Result<UserRegisterResponseModel>>
    fun loginUser(user: User): Observable<Result<UserRegisterResponseModel>>
    fun updateUserInfo(user_token: String?, user: User): Observable<Result<UserInfoResponceModel>>
    fun getUserInfo(user_token: String?): Observable<Result<UserInfoResponceModel>>
    fun addUserPhoneNumber(user_token: String?, phone_number: String): Observable<Result<UserAddPhoneResponceModel>>
    fun activeUserPhoneNumber(
        user_token: String?,
        phone_number: String,
        code: String
    ): Observable<Result<UserRegisterResponseModel>>

    fun addDocument(
        userToken: String?,
        imageSuid: String,
        documentType: UserRepository.DocumentTypeEnum
    ): Observable<Result<FreeResponseModel>>

    fun saveUserInfo(info: User?)
    fun getUserInfo(): User?
    fun saveUserToken(token: String?)
    fun clearLogin()
    fun getUserToken(): String?
    fun isLogin(): Boolean
}

