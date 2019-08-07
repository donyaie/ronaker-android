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
    fun productSearch(@Header("Authorization") authToken: String?, @Query("page") page: Int,@Body request: ProductSearchRequestModel?): Observable<ListResponseModel<ProductItemResponceModel>>

    /**
     * get my created product
     */
    @GET("/api/v1/products/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getMyProduct(@Header("Authorization") authToken: String?): Observable<ListResponseModel<ProductItemResponceModel>>



    /**
     * create new product
     */
    @POST("/api/v1/products/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productCreate(@Header("Authorization") authToken: String?,@Body request: ProductCreateRequestModel): Observable<ProductCreateResponseModel>


    /**
     * create new product
     */
    @PATCH("/api/v1/products/{suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun productUpdate(@Header("Authorization") authToken: String?,@Path("suid") suid:String,@Body request: ProductCreateRequestModel): Observable<ProductCreateResponseModel>




    /**
     * get product detail
     */
    @GET("/api/v1/products/{suid}/")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getProduct(@Header("Authorization") authToken: String?,@Path("suid") suid:String): Observable<ProductDetailResponceModel>


}