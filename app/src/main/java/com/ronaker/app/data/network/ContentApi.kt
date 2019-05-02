package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.ContentImageResponceModel
import com.ronaker.app.data.network.response.ProductSearchResponceModel
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.ResponseBody
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

}