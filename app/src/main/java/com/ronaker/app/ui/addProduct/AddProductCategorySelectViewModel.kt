package com.ronaker.app.ui.addProduct

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Category
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class AddProductCategorySelectViewModel(app:Application) : BaseViewModel(app) {

    internal val TAG = AddProductCategorySelectViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


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

        listAdapter = CategorySelectAdapter(dataList, this)

    }

    override fun onCleared() {
        super.onCleared()
        searchSubscription?.dispose()
        updateproductSubscription?.dispose()

    }

    fun searchLocation(parent:Category?) {

        searchSubscription?.dispose()

        searchSubscription = categoryRepository.getCategories(
            userRepository.getUserToken()
        )
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe { result ->
                if (result .isSuccess()) {

                    dataList.clear()
                    parent?.let {category->

                        result.data?.forEach{
                            if(category.suid.compareTo(it.suid)==0){

                                it.sub_categories?.let {it1->
                                    dataList.addAll(it1)

                                }
                            }
                        }


                    }?:run{result.data?.let { dataList.addAll(it) }}


                    listAdapter.notifyDataSetChanged()

                }
            }


    }

    fun selectCategory(data: Category) {
        selectedPlace.value = data

    }

}