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

class DefaultProductRepository(
    private val productApi: ProductApi,
    private val userRepository: UserRepository
) :
    ProductRepository {


    override fun productSearch(
        query: String?,
        page: Int,
        location: LatLng?,
        radius: Int?,
        isSaved: Boolean?,
        categorySiud: String?
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
            radius,
            isSaved,
            categorySiud
        )



        return productApi.productSearch(userRepository.getUserAuthorization(), page, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toProductList())
            }
            .toResult()

    }

    override fun getMyProduct(
        page: Int
    ): Observable<Result<ListResponseModel<Product>>> {

        return productApi.getMyProduct(userRepository.getUserAuthorization(), page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toProductList())
            }
            .toResult()

    }

    override fun productCreate(
        product: Product
    ): Observable<Result<String?>> {
        return productApi.productCreate(userRepository.getUserAuthorization(), product.toProductCreateModel())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.suid }
            .toResult()

    }

    override fun productUpdate(
        suid: String,
        product: Product
    ): Observable<Result<String?>> {

        return productApi.productUpdate(userRepository.getUserAuthorization(), suid, product.toProductCreateModel())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.suid }
            .toResult()

    }

    override fun getProduct(
        suid: String
    ): Observable<Result<Product>> {
        return productApi.getProduct(userRepository.getUserAuthorization(), suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .observeOn(AndroidSchedulers.mainThread())
            .map { it.toProductDetail() }
            .toResult()
    }


    override fun getProductRating(
        suid: String
    ): Observable<Result<ListResponseModel<Product.ProductRate>?>> {
        return productApi.getProductRate(userRepository.getUserAuthorization(), suid)

            .map {
                ListResponseModel(it.count, it.next, it.previous, it.results?.toProductRateList())
            }
            .toResult()
    }


    override fun productSave(
        suid: String
    ): Observable<Result<Boolean>> {
        return productApi.productSave(userRepository.getUserAuthorization(), suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()
    }


    override fun productSavedRemove(
        suid: String
    ): Observable<Result<Boolean>> {
        return productApi.productSavedRemove(userRepository.getUserAuthorization(), suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()
    }


}