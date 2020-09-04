package com.ronaker.app.ui.explore


import android.app.Application
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.utils.actionSearch
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExploreViewModel @ViewModelInject constructor(
    private val productRepository:ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val analytics: FirebaseAnalytics
) : BaseViewModel(),
    CategoryExploreAdapter.AdapterListener {



    private var page = 1
    private var hasNextPage = true

    private var location: LatLng? = null

    private var selectedCategory: Category? = null


    var dataList: ArrayList<Product> = ArrayList()

    var listView: MutableLiveData<ArrayList<Product>> = MutableLiveData()


    private var categoryList: ArrayList<Category> = ArrayList()
    private var cachCategoryList: ArrayList<Category> = ArrayList()





    private var query: String = ""



    var categoryListAdapter: CategoryExploreAdapter = CategoryExploreAdapter(categoryList, this)

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingEnd: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()

    val searchText: MutableLiveData<String> = MutableLiveData()


    val findNearBy: MutableLiveData<Boolean> = MutableLiveData()

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()
    val locationVisibility: MutableLiveData<Int> = MutableLiveData()
    val locationCheck: MutableLiveData<Boolean> = MutableLiveData()


    val scrollCategoryPosition: MutableLiveData<Int> = MutableLiveData()


    val searchValue: MutableLiveData<String> = MutableLiveData()


    private fun reset() {
        page = 1
        hasNextPage = true
        dataList.clear()
    }

    private var subscription: Disposable? = null

    private var categorySubscription: Disposable? = null

    init {
        reset()
        uiScope.launch {

            loadProduct()
            if (cachCategoryList.isEmpty())
                loadCategory()
        }


    }


    private suspend fun loadCategory() =
        withContext(Dispatchers.Unconfined) {

            categorySubscription?.dispose()
            categorySubscription = categoryRepository
                .getCategories()
                .doOnSubscribe { }
                .doOnTerminate { }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        if ((result.data?.size ?: 0) > 0) {


                            result.data?.let {


                                cachCategoryList = ArrayList(result.data)

                                if (selectedCategory == null) {

                                    categoryList.clear()
                                    categoryList.addAll(cachCategoryList)

                                    categoryListAdapter.reset()
                                    categoryListAdapter.updateList()

                                }


                            }


                        }
                    }
                }


        }

    suspend fun loadProduct() =

        withContext(Dispatchers.Unconfined) {

//            if (location == null) {
////                emptyVisibility.postValue(View.VISIBLE)
//
//                loading.postValue(false)
//                locationCheck.postValue(true)
//                return@withContext
//            }

            if (hasNextPage) {

                subscription?.dispose()


                var searchValue: String? = query

                if (searchValue.isNullOrBlank())
                    searchValue = null


                subscription = productRepository
                    .productSearch(

                        query = searchValue,
                        page = page,
                        location = location,
                        radius = 50,
                        categorySiud = selectedCategory?.suid
                    )
                    .doOnSubscribe {
                        retry.postValue(null)
                        if (page <= 1) {
                            loading.postValue(true)

                            emptyVisibility.postValue(View.GONE)

                        }else{
                            loadingEnd.postValue(true)
                        }

                        errorMessage.postValue(null)
                    }
                    .doOnTerminate {
                        loadingEnd.postValue(false)
                        loading.postValue(false)
                    }
                    .subscribe { result ->
                        if (result.isSuccess()) {
                            page++
                            result.data?.results?.let { dataList.addAll(it) }

                            listView.postValue(dataList)

                            if (!result.data?.results.isNullOrEmpty()) {
                                emptyVisibility.postValue(View.GONE)
                            }

                            if (result.data?.next == null) {
                                hasNextPage = false
                            }


                            if (dataList.isEmpty()) {
                                emptyVisibility.postValue(View.VISIBLE)
                            }


                            if(page==2 && hasNextPage){
                                loadMore()
                            }

                        } else {

                            if (page <= 1)
                                retry.postValue(result.error?.message)
                            else
                                errorMessage.postValue(result.error?.message)

                        }
                    }
            }
        }


    fun onClickSearch() {

        searchValue.postValue("")

    }


    override fun onCleared() {
        super.onCleared()
        categorySubscription?.dispose()
        subscription?.dispose()
    }

    fun loadMore() {


            uiScope.launch {

                loadProduct()
            }

    }

    fun retry() {
        reset()

        uiScope.launch {

            loadProduct()
            if (cachCategoryList.isEmpty())
                loadCategory()
        }

    }

    fun search(search: String) {

        if (search.isNotEmpty()) {
            analytics.actionSearch(search)
        }

        reset()
        query = search

        updateSearchCaption()

        uiScope.launch {

            loadProduct()
        }


    }

    fun clearSearch() {

        clearSelectCategory()
        search("")
    }

    override fun onSelectCategory(selected: Category) {


        if (selected == selectedCategory)
            return

        if (selected.sub_categories.isNullOrEmpty()) {
            categoryList.forEach {
                it.isSelected = false

            }
            selected.isSelected = true



            selectedCategory?.let {
                categoryListAdapter.itemChanged(it)

            }

            categoryListAdapter.itemChanged(selected)

            scrollCategoryPosition.postValue(categoryList.indexOf(selected))


        } else {
            categoryList.clear()
            categoryList.addAll(selected.sub_categories as ArrayList<Category>)

            categoryList.forEach {
                it.isSelected = false

            }
            categoryListAdapter.reset()
            categoryListAdapter.updateList()
            scrollCategoryPosition.postValue(0)

        }


        selectedCategory = selected

        search(query)


    }

    private fun clearSelectCategory() {

        selectedCategory = null
        categoryList.clear()

        categoryList.addAll(cachCategoryList)


        categoryListAdapter.reset()
        categoryListAdapter.updateList()


    }

    private fun updateSearchCaption() {

        searchText.postValue(
            query + (selectedCategory?.let { " in " + selectedCategory?.title } ?: run { "" })
        )


    }

    fun backPress(): Boolean {

        var handle = false

        if (selectedCategory != null || query.isNotEmpty()) {
            clearSearch()
            handle = true
        }

        return handle
    }


    fun setLocation(lastLocation: LatLng?) {

        location = lastLocation
        retry()


    }




}