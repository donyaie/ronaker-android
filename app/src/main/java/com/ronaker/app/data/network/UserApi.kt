package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.*
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface UserApi {
    /**
     * Register new user
     */
    @POST("/api/v1/users/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun registerUser(@Body user: UserRegisterRequestModel): Observable<UserRegisterResponseModel>

    /**
     * log in user with user and password
     */
    @POST("/api/v1/users/token_auth/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun loginUser(@Body user: UserLoginRequestModel): Observable<UserRegisterResponseModel>

    /**
     * get user Info with token
     */
    @GET("/api/v1/users/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getUserInfo(@Header("Authorization") authToken: String): Observable<UserInfoResponceModel>

    /**
     * add phone Checkout to user and recive otp
     */
    @POST("/api/v1/users/phone_number/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addUserPhoneNumber(@Header("Authorization") authToken: String,@Body user: UserAddPhoneRequestModel): Observable<UserAddPhoneResponceModel>

    /**
     * update user Info
     */
    @PUT("/api/v1/users/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun updateUserInfo(@Header("Authorization") authToken: String,@Body user: UserUpdateRequestModel): Observable<UserInfoResponceModel>

    /**
     * valid phone Checkout of user with otp
     */
    @POST("/api/v1/users/phone_number/activation/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun activeUserPhoneNumber(@Header("Authorization") authToken: String,@Body user: UserActivePhoneRequestModel): Observable<UserAddPhoneResponceModel>

    /**
     * add user identify
     */
    @POST("/api/v1/documents/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addDocument(@Header("Authorization") authToken: String,@Body request: UserIdentifyRequestModel): Observable<FreeResponseModel>

    /**
     * send Email verification
     */
    @GET("/api/v1/users/email-verification/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun sendEmailVerification(@Header("Authorization") authToken: String): Observable<FreeResponseModel>

    /**
     * Get SmartID Verification Code
     */
    @POST("/api/v1/users/smart-id/code/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getSmartIDVerificationCode(@Header("Authorization") authToken: String,@Body request: UserSmartIdVerificationCodeRequestModel): Observable<UserSmartIdVerificationResponseModel>

    /**
     * Start SmartID Auth
     */
    @POST("/api/v1/users/smart-id/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun startSmartIDAuth(@Header("Authorization") authToken: String,@Body request: UserSmartIdVerificationCodeRequestModel): Observable<FreeResponseModel>

    /**
     * Check smart-id session
     */
    @GET("/api/v1/users/smart-id/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun checkSmartIDSession(@Header("Authorization") authToken: String): Observable<FreeResponseModel>
}