package com.ronaker.app.data


import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.model.Product
import io.reactivex.Observable

interface ProductRepository {
    fun productSearch(token: String?, query: String?, page: Int, location: LatLng?, radius: Int?, isSaved:Boolean?=null,
                      categorySiud:String?=null): Observable<Result<ListResponseModel<Product>>>
    fun getMyProduct(token: String?, page: Int): Observable<Result<ListResponseModel<Product>>>
    fun productCreate(token: String?, product: Product): Observable<Result<String?>>
    fun productUpdate(token: String?, suid: String, product: Product): Observable<Result<String?>>
    fun getProduct(token: String?, suid: String): Observable<Result<Product>>
    fun getProductRating(
        token: String?,
        suid: String
    ): Observable<Result<ListResponseModel<Product.ProductRate>?>>

    fun productSavedRemove(token: String?, suid: String): Observable<Result<Boolean>>
    fun productSave(token: String?, suid: String): Observable<Result<Boolean>>
}

