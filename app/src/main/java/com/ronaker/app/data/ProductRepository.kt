package com.ronaker.app.data


import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.data.network.response.ProductCreateResponseModel
import com.ronaker.app.data.network.response.ProductDetailResponceModel
import com.ronaker.app.data.network.response.ProductItemResponceModel
import com.ronaker.app.model.Product
import io.reactivex.Observable

interface ProductRepository {
    fun productSearch(token: String?, query: String?, page: Int, location: LatLng?, radius: Int?): Observable<Result<ListResponseModel<ProductItemResponceModel>>>
    fun getMyProduct(token: String?, page: Int): Observable<Result<ListResponseModel<ProductItemResponceModel>>>
    fun productCreate(token: String?, product: Product): Observable<Result<ProductCreateResponseModel>>
    fun productUpdate(token: String?, suid: String, product: Product): Observable<Result<ProductCreateResponseModel>>
    fun getProduct(token: String?, suid: String): Observable<Result<ProductDetailResponceModel>>
}

