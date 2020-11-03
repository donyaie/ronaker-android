package com.ronaker.app.data


import com.google.gson.reflect.TypeToken
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
import java.lang.reflect.Type
import javax.inject.Inject

class DefaultCategoryRepository @Inject constructor(
    private val api: CategoryApi,
    private val preferencesProvider: PreferencesDataSource,
    private val userRepository: UserRepository
) : CategoryRepository {

    private val CategoryKey = "category_key"

    override fun getCategories(): Observable<Result<List<Category>?>> {

        return api.getCategories(userRepository.getUserAuthorization(),userRepository.getUserLanguage())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {


                it.results?.toCategoryList()
                    .apply {
                    this?.let {

                        saveCategories(ArrayList(this))
                    }
                }


            }
            .toResult()
    }


    override fun saveCategories(value: ArrayList<Category>?) {

        preferencesProvider.putObjectList<Category>(CategoryKey, value)
    }

    override fun getCategoriesLocal(): ArrayList<Category>? {

        val listType: Type = object : TypeToken<ArrayList<Category>>() {}.type
        return preferencesProvider.getObjectList(CategoryKey,listType)
    }


}

