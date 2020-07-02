package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.model.DocumentTypeEnum
import com.ronaker.app.model.User
import io.reactivex.Observable

interface UserRepository {


    fun registerUser(user: User): Observable<Result<User>>
    fun loginUser(user: User): Observable<Result<User>>
    fun updateUserInfo(user_token: String?, user: User): Observable<Result<User>>
    fun getUserInfo(user_token: String?): Observable<Result<User>>
    fun addUserPhoneNumber(user_token: String?, phone_number: String): Observable<Result<String>>
    fun activeUserPhoneNumber(
        user_token: String?,
        phone_number: String,
        code: String
    ): Observable<Result<String>>

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
    fun sendEmailVerification(user_token: String?): Observable<Result<Boolean>>

    fun getSmartIDVerificationCode(
        user_token: String?,
        national_code: String,
        personal_code: String
    ): Observable<Result<String>>

    fun checkSmartIDSession(user_token: String?): Observable<Result<Boolean>>
    fun startSmartIDAuth(
        user_token: String?,
        national_code: String,
        personal_code: String
    ): Observable<Result<Boolean>>
}

