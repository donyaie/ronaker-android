package com.ronaker.app.data

import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ProductApi
import com.ronaker.app.data.network.request.ProductSearchRequestModel
import com.ronaker.app.data.network.response.ListResponseModel
import com.ronaker.app.data.network.response.LocationResponseModel
import com.ronaker.app.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultProductRepository(private val productApi: ProductApi) :
    ProductRepository {


    override fun productSearch(
        token: String?,
        query: String?,
        page: Int,
        location: LatLng?,
        radius: Int?
    ): Observable<Result<ListResponseModel<Product>>> {

        val loc = location?.let {
            LocationResponseModel(
                it.latitude,
                it.longitude
            )
        }


        val request = ProductSearchRequestModel(
            query,
            loc,
            radius
        )



        return productApi.productSearch("Token $token", page, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toProductList())
            }
            .toResult()

    }

    override fun getMyProduct(
        token: String?,
        page: Int
    ): Observable<Result<ListResponseModel<Product>>> {

        return productApi.getMyProduct("Token $token", page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toProductList())
            }
            .toResult()

    }

    override fun productCreate(
        token: String?,
        product: Product
    ): Observable<Result<String?>> {
        return productApi.productCreate("Token $token", product.toProductCreateModel())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.suid }
            .toResult()

    }

    override fun productUpdate(
        token: String?,
        suid: String,
        product: Product
    ): Observable<Result<String?>> {

        return productApi.productUpdate("Token $token", suid, product.toProductCreateModel())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.suid }
            .toResult()

    }

    override fun getProduct(
        token: String?,
        suid: String
    ): Observable<Result<Product>> {
        return productApi.getProduct("Token $token", suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.toProductDetail() }
            .toResult()
    }



    override fun getProductRating(
        token: String?,
        suid: String
    ): Observable<Result<ListResponseModel<Product.ProductRate>?>> {
        return productApi.getProductRate("Token $token", suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toProductRateList())
            }
            .toResult()
    }


}