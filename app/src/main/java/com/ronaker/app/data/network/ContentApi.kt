package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.ContentImageResponseModel
import com.ronaker.app.data.network.response.FreeResponseModel
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface ContentApi {
    /**
     * upload  new Image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImage(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Single<ResponseBody>




    /**
     * delete Image
     */
    @DELETE("/api/v1/contents/images/{suid}/")
    fun deleteImage(@Header("Authorization") authToken: String?, @Path("suid") suid:String ): Observable<FreeResponseModel>


    /**
     * upload  new Image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgress(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Observable<ContentImageResponseModel>




    /**
     * upload  new Image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgressr(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Call<ContentImageResponseModel>

}