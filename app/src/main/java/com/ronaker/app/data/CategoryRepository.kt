package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.CategoriesResponseModel
import com.ronaker.app.data.network.response.ListResponseModel
import io.reactivex.Observable

interface CategoryRepository {
    fun getCategories(token: String?): Observable<Result<ListResponseModel<CategoriesResponseModel>>>
}

