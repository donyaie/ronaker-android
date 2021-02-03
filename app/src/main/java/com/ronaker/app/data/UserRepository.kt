package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.StripeSetupResponseModel
import com.ronaker.app.model.DocumentTypeEnum
import com.ronaker.app.model.User
import io.reactivex.Observable

interface UserRepository {


    fun registerUser(user: User): Observable<Result<User>>
    fun loginUser(user: User): Observable<Result<User>>
    fun updateUserInfo( user: User): Observable<Result<User>>
    fun getUserInfo(user_auth: String): Observable<Result<User>>
    fun addUserPhoneNumber( phone_number: String): Observable<Result<String>>
    fun activeUserPhoneNumber(
        phone_number: String,
        code: String
    ): Observable<Result<String>>

    fun addDocument(
        imageSuid: String,
        documentType: DocumentTypeEnum
    ): Observable<Result<Boolean>>

    fun saveUserInfo(info: User?)
    fun getUserInfo(): User?
    fun saveUserToken(token: String?)
    fun clearLogin()
    fun getUserToken(): String?
    fun isLogin(): Boolean
    fun sendEmailVerification(): Observable<Result<Boolean>>

    fun getSmartIDVerificationCode(
        national_code: String,
        personal_code: String
    ): Observable<Result<String>>

    fun checkSmartIDSession(): Observable<Result<Boolean>>
    fun startSmartIDAuth(
        national_code: String,
        personal_code: String
    ): Observable<Result<Boolean>>


    fun stripeSetup(
    ): Observable<Result<StripeSetupResponseModel>>


    fun forgetPassword(email: String): Observable<Result<Boolean>>
    fun forgetPasswordConfirm(token: String, password: String): Observable<Result<Boolean>>
    fun getUserAuthorization(): String
    fun getUserLanguage(): String
    fun getPrivacyUrl(): String
    fun getTermUrl(): String
    fun loginUserWithGoogle(token: String): Observable<Result<User>>
    fun saveUpdateSkipVersion(updateVersion: Int)
    fun getUpdateSkipVersion(): Int
}

