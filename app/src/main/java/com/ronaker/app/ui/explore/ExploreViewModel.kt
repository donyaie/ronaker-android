package com.ronaker.app.ui.explore


import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.utils.actionSearch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExploreViewModel(app: Application) : BaseViewModel(app),
    CategoryExploreAdapter.AdapterListener {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    @Inject
    lateinit
    var categoryRepository: CategoryRepository


    private var page = 0
    private var hasNextPage = true


  private  var selectedCategory: Category? = null


    var dataList: ArrayList<Product> = ArrayList()

    private var categoryList: ArrayList<Category> = ArrayList()
    private var cachCategoryList: ArrayList<Category> = ArrayList()


    private var query: String = ""


    var productListAdapter: ItemExploreAdapter = ItemExploreAdapter(dataList)


    var categoryListAdapter: CategoryExploreAdapter = CategoryExploreAdapter(categoryList, this)

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    val searchText: MutableLiveData<String> = MutableLiveData()

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()
    val scrollCategoryPosition: MutableLiveData<Int> = MutableLiveData()


    val searchValue: MutableLiveData<String> = MutableLiveData()


    private fun reset() {

        page = 0
        productListAdapter.reset()
        hasNextPage = true
        dataList.clear()
        productListAdapter.updateList()
        resetList.postValue(true)
    }

    private var subscription: Disposable? = null

    private var categorySubscription: Disposable? = null

    init {
        reset()
        uiScope.launch {

            loadCategory()
            loadProduct()
        }

    }


    private suspend fun loadCategory() =
        withContext(Dispatchers.IO) {

            categorySubscription?.dispose()
            categorySubscription = categoryRepository
                .getCategories(userRepository.getUserToken())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
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
        withContext(Dispatchers.IO) {
            if (hasNextPage) {
                page++
                subscription?.dispose()


                var searchValue: String? = query

                if (searchValue.isNullOrBlank())
                    searchValue = null

                subscription = productRepository
                    .productSearch(
                        userRepository.getUserToken(),
                        searchValue,
                        page,
                        null,
                        null,
                        categorySiud = selectedCategory?.suid
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnSubscribe { onRetrieveProductListStart() }
                    .doOnTerminate { onRetrieveProductListFinish() }
                    .subscribe { result ->
                        if (result.isSuccess()) {
                            if ((result.data?.results?.size ?: 0) > 0) {

                                emptyVisibility.postValue(View.GONE)
                                onRetrieveProductListSuccess(
                                    result.data?.results
                                )

                                if (result.data?.next == null) {
                                    hasNextPage = false


                                }

                            } else {

                                emptyVisibility.postValue(View.VISIBLE)




                                hasNextPage = false
                            }
                        } else {

                            onRetrieveProductListError(result.error)

//                        onRetrieveProductListSuccess(
//                            dataList
//                        )
                        }
                    }
            }
        }


    private fun onRetrieveProductListStart() {
        retry.postValue(null)
        if (page <= 1) {
            loading.postValue(true)

            emptyVisibility.postValue(View.GONE)

        }
        errorMessage.postValue(null)
    }

    private fun onRetrieveProductListFinish() {
        loading.postValue(false)
    }

    private fun onRetrieveProductListSuccess(productList: List<Product>?) {

        if (productList != null) {
            productListAdapter.addData(productList)
        }

    }

    private fun onRetrieveProductListError(error: NetworkError?) {
        if (page <= 1)
            retry.postValue(error?.message)
        else
            errorMessage.postValue(error?.message)


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
            loadCategory()
        }

    }

    fun search(search: String) {

        if (search.isNotEmpty()) {
            getAnalytics()?.actionSearch(search)
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

}