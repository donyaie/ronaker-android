package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.ContentImageResponseModel
import com.ronaker.app.data.network.response.FreeResponseModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 * The interface which provides methods to get result of webservices
 */
interface ContentApi {

    /**
     * delete Image
     */
    @DELETE("/api/v1/contents/images/{suid}/")
    fun deleteImage(
        @Header("Authorization") authToken: String?, @Header("Accept-Language") language: String,
        @Path("suid") suid: String
    ): Observable<FreeResponseModel>


    /**
     * upload  new Image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgress(
        @Header("Authorization") authToken: String?, @Header("Accept-Language") language: String,
        @Part file: MultipartBody.Part
    ): Observable<ContentImageResponseModel>


    /**
     * download
     */
    @GET
    @Streaming
    fun download(
        @Header("Authorization") authToken: String?, @Header("Accept-Language") language: String,
        @Url fileUrl: String
    ): Observable<Response<ResponseBody>>

}