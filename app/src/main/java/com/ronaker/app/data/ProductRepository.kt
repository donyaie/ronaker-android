package com.ronaker.app.data

import com.ronaker.app.base.PreferencesProvider
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ProductApi
import com.ronaker.app.data.network.response.ProductSearchResponceModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductRepository(private val productApi: ProductApi, private val preferencesProvider: PreferencesProvider) {



    fun productSearch(token: String?, page:Int): Observable<Result<ProductSearchResponceModel>> {


        return productApi.productSearch("Token $token",page )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }


}

