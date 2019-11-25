package com.ronaker.app.data


import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.CategoryApi
import com.ronaker.app.data.network.response.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CategoryRepository(private val api: CategoryApi) {



    fun getCategories(token: String?): Observable<Result<ListResponseModel<CategoriesResponseModel>>> {

        return api.getCategories("Token $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }



}

