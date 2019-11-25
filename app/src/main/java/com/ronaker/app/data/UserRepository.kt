package com.ronaker.app.data

import com.ronaker.app.base.Result
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
            operator fun get(position: String): DocumentTypeEnum {
                var state = None
                for (stateEnum in values()) {
                    if (position.compareTo(stateEnum.key) == 0)
                        state = stateEnum
                }
                return state
            }
        }

    }

    fun registerUser(user: User): Observable<Result<User>>
    fun loginUser(user: User): Observable<Result<User>>
    fun updateUserInfo(user_token: String?, user: User): Observable<Result<User>>
    fun getUserInfo(user_token: String?): Observable<Result<User>>
    fun addUserPhoneNumber(user_token: String?, phone_number: String): Observable<Result<String>>
    fun activeUserPhoneNumber(
        user_token: String?,
        phone_number: String,
        code: String
    ): Observable<Result<User>>

    fun addDocument(
        userToken: String?,
        imageSuid: String,
        documentType: DocumentTypeEnum
    ): Observable<Result<Boolean>>

    fun saveUserInfo(info: User?)
    fun getUserInfo(): User?
    fun saveUserToken(token: String?)
    fun clearLogin()
    fun getUserToken(): String?
    fun isLogin(): Boolean
}

