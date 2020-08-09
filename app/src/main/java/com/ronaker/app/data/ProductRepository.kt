package com.ronaker.app.data


import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.Product
import io.reactivex.Observable

interface ProductRepository {
    fun productSearch(
        query: String?,
        page: Int,
        location: LatLng?,
        radius: Int?,
        isSaved: Boolean? = null,
        categorySiud: String? = null
    ): Observable<Result<ListResponseModel<Product>>>

    fun getMyProduct( page: Int): Observable<Result<ListResponseModel<Product>>>
    fun productCreate( product: Product): Observable<Result<String?>>
    fun productUpdate( suid: String, product: Product): Observable<Result<String?>>
    fun getProduct(suid: String): Observable<Result<Product>>
    fun getProductRating(
        suid: String
    ): Observable<Result<ListResponseModel<Product.ProductRate>?>>

    fun productSavedRemove( suid: String): Observable<Result<Boolean>>
    fun productSave( suid: String): Observable<Result<Boolean>>
}

