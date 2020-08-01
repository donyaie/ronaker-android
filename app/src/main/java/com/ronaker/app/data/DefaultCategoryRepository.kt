package com.ronaker.app.data


import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.local.PreferencesDataSource
import com.ronaker.app.data.network.CategoryApi
import com.ronaker.app.model.Category
import com.ronaker.app.model.User
import com.ronaker.app.model.toCategoryList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DefaultCategoryRepository(
    private val api: CategoryApi,
    private val preferencesProvider: PreferencesDataSource
) : CategoryRepository {

    private val CategoryKey = "category_key"

    override fun getCategories(token: String?): Observable<Result<List<Category>?>> {

        return api.getCategories("Token $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {


                it.results?.toCategoryList().apply {
                    saveCategories(this)
                }


            }
            .toResult()
    }



    override fun saveCategories(value: List<Category>?) {
        preferencesProvider.putObject(CategoryKey, value)
    }

    override fun getCategories(): List<Category>? {
        return preferencesProvider.getObject(CategoryKey, User::class.java)
    }



}

