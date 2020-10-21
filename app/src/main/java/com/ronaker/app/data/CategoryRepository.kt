package com.ronaker.app.data

import com.ronaker.app.base.Result
import com.ronaker.app.model.Category
import io.reactivex.Observable

interface CategoryRepository {
    fun getCategories(): Observable<Result<List<Category>?>>
    fun saveCategories(value: ArrayList<Category>?)
    fun getCategoriesLocal(): ArrayList<Category>?
}

