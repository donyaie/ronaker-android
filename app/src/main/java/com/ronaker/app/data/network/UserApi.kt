package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.UserActivePhoneRequestModel
import com.ronaker.app.data.network.request.UserAddPhoneRequestModel
import com.ronaker.app.data.network.request.UserLoginRequestModel
import com.ronaker.app.data.network.request.UserRegisterRequestModel
import com.ronaker.app.data.network.response.UserAddPhoneResponceModel
import com.ronaker.app.data.network.response.UserInfoResponceModel
import com.ronaker.app.data.network.response.UserRegisterResponseModel
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
     * get user info with token
     */
    @GET("/api/v1/users/?")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getUserInfo(@Header("Authorization") authToken: String): Observable<UserInfoResponceModel>

    /**
     * add phone number to user and recive otp
     */
    @POST("/api/v1/users/phone_number/?")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun addUserPhoneNumber(@Header("Authorization") authToken: String,@Body user: UserAddPhoneRequestModel): Observable<UserAddPhoneResponceModel>


    /**
     * valid phone number of user with otp
     */
    @POST("/api/v1/users/phone_number/activation/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun activeUserPhoneNumber(@Header("Authorization") authToken: String,@Body user: UserActivePhoneRequestModel): Observable<UserRegisterResponseModel>
}