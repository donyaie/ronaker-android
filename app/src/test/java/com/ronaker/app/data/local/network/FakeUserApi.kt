package com.ronaker.app.data.local.network
//
//import com.ronaker.app.data.network.UserApi
//import com.ronaker.app.data.network.request.*
//import com.ronaker.app.data.network.response.FreeResponseModel
//import com.ronaker.app.data.network.response.UserAddPhoneResponceModel
//import com.ronaker.app.data.network.response.UserInfoResponceModel
//import com.ronaker.app.data.network.response.UserRegisterResponseModel
//import io.reactivex.Observable
//
//
//class FakeUserApi :UserApi {
//    override fun registerUser(user: UserRegisterRequestModel): Observable<UserRegisterResponseModel> {
//    }
//
//    override fun loginUser(user: UserLoginRequestModel): Observable<UserRegisterResponseModel> {
//    }
//
//    override fun getUserInfo(authToken: String): Observable<UserInfoResponceModel> {
//    }
//
//    override fun addUserPhoneNumber(
//        authToken: String,
//        user: UserAddPhoneRequestModel
//    ): Observable<UserAddPhoneResponceModel> {
//    }
//
//    override fun updateUserInfo(
//        authToken: String,
//        user: UserUpdateRequestModel
//    ): Observable<UserInfoResponceModel> {
//    }
//
//    override fun activeUserPhoneNumber(
//        authToken: String,
//        user: UserActivePhoneRequestModel
//    ): Observable<UserAddPhoneResponceModel> {
//    }
//
//    override fun addDocument(
//        authToken: String,
//        request: UserIdentifyRequestModel
//    ): Observable<FreeResponseModel> {
//    }
//
//}