package com.ronaker.app.ui.selectCategory

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.model.Category
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class AddProductCategorySelectViewModel(app: Application) : BaseViewModel(app) {

    internal val TAG = AddProductCategorySelectViewModel::class.java.name



    @Inject
    lateinit var context: Context

    @Inject
    lateinit var categoryRepository: CategoryRepository


    private var searchSubscription: Disposable? = null
    private var updateproductSubscription: Disposable? = null

    val selectedPlace: MutableLiveData<Category> = MutableLiveData()


    var dataList: ArrayList<Category> = ArrayList()
    var listAdapter: CategorySelectAdapter


    init {

        listAdapter = CategorySelectAdapter(
            dataList,
            this
        )

    }

    override fun onCleared() {
        super.onCleared()
        searchSubscription?.dispose()
        updateproductSubscription?.dispose()

    }

    fun searchLocation(categoryList:List<Category>) {



        dataList.clear()

        dataList.addAll(categoryList)


        listAdapter.notifyDataSetChanged()


    }

    fun selectCategory(data: Category) {
        selectedPlace.value = data

    }

}