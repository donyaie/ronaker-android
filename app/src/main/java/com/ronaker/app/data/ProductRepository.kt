package com.ronaker.app.data


import com.ronaker.app.base.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ProductApi
import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.response.ProductCreateResponseModel
import com.ronaker.app.data.network.response.ProductDetailResponceModel
import com.ronaker.app.data.network.response.ProductSearchResponceModel
import com.ronaker.app.model.Product
import com.ronaker.app.model.toProductCreateModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductRepository(private val productApi: ProductApi, private val preferencesProvider: PreferencesProvider) {



    fun productSearch(token: String?, page:Int): Observable<Result<ProductSearchResponceModel>> {


        return productApi.productSearch("Token $token",page )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun productCreate(token: String?, product:Product): Observable<Result<ProductCreateResponseModel>> {
        return productApi.productCreate("Token $token",product.toProductCreateModel() )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun getProduct(token: String?, suid:String): Observable<Result<ProductDetailResponceModel>> {
        return productApi.getProduct("Token $token",suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }




}

