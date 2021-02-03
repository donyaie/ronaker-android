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
    fun registerUser(@Header("Accept-Language") language: String,@Body user: UserRegisterRequestModel): Observable<UserRegisterResponseModel>

    /**
     * Setup stripe
     */
    @GET("/api/v1/users/stripe/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun stripeSetup(@Header("Accept-Language") language: String,@Header("Authorization") authToken: String): Observable<StripeSetupResponseModel>



    /**
     * log in user with user and password
     */
    @POST("/api/v1/users/token_auth/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun loginUser(@Header("Accept-Language") language: String,@Body user: UserLoginRequestModel): Observable<UserRegisterResponseModel>

    /**
     * get user Info with token
     */
    @GET("/api/v1/users/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getUserInfo(@Header("Accept-Language") language: String,@Header("Authorization") authToken: String): Observable<UserInfoResponceModel>

    /**
     * add phone Checkout to user and recive otp
     */
    @POST("/api/v1/users/phone_number/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addUserPhoneNumber(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body user: UserAddPhoneRequestModel
    ): Observable<UserAddPhoneResponceModel>

    /**
     * update user Info
     */
    @PUT("/api/v1/users/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun updateUserInfo(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body user: UserUpdateRequestModel
    ): Observable<UserInfoResponceModel>

    /**
     * valid phone Checkout of user with otp
     */
    @POST("/api/v1/users/phone_number/activation/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun activeUserPhoneNumber(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body user: UserActivePhoneRequestModel
    ): Observable<UserAddPhoneResponceModel>

    /**
     * add user identify
     */
    @POST("/api/v1/documents/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addDocument(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body request: UserIdentifyRequestModel
    ): Observable<FreeResponseModel>

    /**
     * send Email verification
     */
    @GET("/api/v1/users/email-verification/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun sendEmailVerification(@Header("Authorization") authToken: String,@Header("Accept-Language") language: String): Observable<FreeResponseModel>

    /**
     * Get SmartID Verification Code
     */
    @POST("/api/v1/users/smart-id/code/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getSmartIDVerificationCode(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body request: UserSmartIdVerificationCodeRequestModel
    ): Observable<UserSmartIdVerificationResponseModel>

    /**
     * Start SmartID Auth
     */
    @POST("/api/v1/users/smart-id/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun startSmartIDAuth(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Body request: UserSmartIdVerificationCodeRequestModel
    ): Observable<FreeResponseModel>

    /**
     * Check smart-id session
     */
    @GET("/api/v1/users/smart-id/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun checkSmartIDSession(@Header("Authorization") authToken: String,@Header("Accept-Language") language: String): Observable<FreeResponseModel>



    /**
     * Forget password
     */
    @POST("/api/v1/users/password_reset/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun forgetPassword(@Header("Accept-Language") language: String,
        @Body request: UserForgetPasswordRequestModel
    ): Observable<FreeResponseModel>




    /**
     * Login with google
     */
    @POST("/api/v1/users/google-login/")
    @Headers("Content-Type:application/json; charset=UTF-8")
     fun loginGoogle(@Header("Accept-Language") language: String,@Body user: UserGoogleLoginResponseModel): Observable< UserRegisterResponseModel>


    /**
     * Forget password confirm
     */
    @POST("/api/v1/users/password_reset/confirm/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun forgetPasswordConfirm(@Header("Accept-Language") language: String,
        @Body request: UserForgetPasswordConfirmRequestModel
    ): Observable<FreeResponseModel>
}