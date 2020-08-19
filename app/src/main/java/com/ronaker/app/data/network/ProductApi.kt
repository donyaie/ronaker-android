package com.ronaker.app.data.network

import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.request.ProductSearchRequestModel
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface ProductApi {
    /**
     * search in products
     */
    @POST("/api/v1/products/search")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productSearch(
        @Header("Authorization") authToken: String?,@Header("Accept-Language") language: String,
        @Query("page") page: Int,
        @Body request: ProductSearchRequestModel?
    ): Observable<ListResponseModel<ProductItemResponseModel>>

    /**
     * get my created mProduct
     */
    @GET("/api/v1/products/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getMyProduct(
        @Header("Authorization") authToken: String?,@Header("Accept-Language") language: String,
        @Query("page") page: Int
    ): Observable<ListResponseModel<ProductItemResponseModel>>

    /**
     * create new mProduct
     */
    @POST("/api/v1/products/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productCreate(
        @Header("Authorization") authToken: String?,@Header("Accept-Language") language: String,
        @Body request: ProductCreateRequestModel
    ): Observable<ProductCreateResponseModel>


    /**
     * create new mProduct
     */
    @PATCH("/api/v1/products/{suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productUpdate(
        @Header("Authorization") authToken: String?,@Header("Accept-Language") language: String,
        @Path("suid") suid: String,
        @Body request: ProductCreateRequestModel
    ): Observable<ProductCreateResponseModel>

    /**
     * get mProduct detail
     */
    @GET("/api/v1/products/{suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getProduct(
        @Header("Authorization") authToken: String?,@Header("Accept-Language") language: String,
        @Path("suid") suid: String
    ): Observable<ProductDetailResponseModel>

    /**
     * get rate List
     */
    @GET("/api/v1/products/{product_suid}/ratings")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getProductRate(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("product_suid") suid: String
    ): Observable<ListResponseModel<ProductRatingResponceModel>>

    /**
     * save product to fave item
     */
    @POST("/api/v1/products/{product_suid}/favourite")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productSave(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("product_suid") suid: String
    ): Observable<FreeResponseModel>

    /**
     * save product to fave item
     */
    @DELETE("/api/v1/products/{product_suid}/favourite")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productSavedRemove(
        @Header("Authorization") authToken: String,@Header("Accept-Language") language: String,
        @Path("product_suid") suid: String
    ): Observable<FreeResponseModel>
}