package com.ronaker.app.data


import com.ronaker.app.base.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ProductApi
import com.ronaker.app.data.network.request.ProductCreateRequestModel
import com.ronaker.app.data.network.request.ProductSearchRequestModel
import com.ronaker.app.data.network.response.*
import com.ronaker.app.model.Product
import com.ronaker.app.model.toProductCreateModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductRepository(private val productApi: ProductApi, private val preferencesProvider: PreferencesProvider) {



    fun productSearch(token: String?,query:String?, page:Int): Observable<Result<ListResponseModel<ProductItemResponceModel>>> {


      var request:ProductSearchRequestModel?=null

//          if(query!=null)
              request= ProductSearchRequestModel(query)


        return productApi.productSearch("Token $token",page, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun getMyProduct(token: String?): Observable<Result<ListResponseModel<ProductItemResponceModel>>> {


        return productApi.getMyProduct("Token $token" )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun productCreate(token: String?, product:Product): Observable<Result<ProductCreateResponseModel>> {
        return productApi.productCreate("Token $token",product.toProductCreateModel() )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun productUpdate(token: String?,suid:String, product:Product): Observable<Result<ProductCreateResponseModel>> {

        return productApi.productUpdate("Token $token",suid,product.toProductCreateModel() )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }

    fun getProduct(token: String?, suid:String): Observable<Result<ProductDetailResponceModel>> {
        return productApi.getProduct("Token $token",suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }




}

