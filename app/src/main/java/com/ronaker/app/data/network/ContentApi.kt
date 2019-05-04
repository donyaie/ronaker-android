package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.ContentImageResponceModel
import com.ronaker.app.data.network.response.ProductSearchResponceModel
import com.ronaker.app.model.Post
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface ContentApi {
    /**
     * Register new user
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImage(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Single<ResponseBody>



    /**
     * Register new user
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgress(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Observable<ContentImageResponceModel>




    /**
     * Register new user
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgressr(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Call<ContentImageResponceModel>

}