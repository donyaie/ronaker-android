package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.ContentImageResponceModel
import com.ronaker.app.data.network.response.ProductCreateResponseModel
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
     * upload  new image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImage(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Single<ResponseBody>




    /**
     * delete image
     */
    @POST("/api/v1/contents/images/{suid}/")
    @Multipart
    fun deleteImage(@Header("Authorization") authToken: String?, @Path("suid") suid:String ): Observable<ProductCreateResponseModel>


    /**
     * upload  new image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgress(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Observable<ContentImageResponceModel>




    /**
     * upload  new image
     */
    @POST("/api/v1/contents/images/")
    @Multipart
    fun uploadImageWithoutProgressr(@Header("Authorization") authToken: String?, @Part file: MultipartBody.Part ): Call<ContentImageResponceModel>

}