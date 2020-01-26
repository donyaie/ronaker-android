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
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ExploreViewModel(app: Application) : BaseViewModel(app) {

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


    var selectedCategory:Category?=null


    var dataList: ArrayList<Product> = ArrayList()

    var categoryList: ArrayList<Category> = ArrayList()


    private var query: String? = null


    var productListAdapter: ItemExploreAdapter= ItemExploreAdapter(dataList)


    var categoryListAdapter: CategoryExploreAdapter= CategoryExploreAdapter(categoryList)

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()


    val searchValue: MutableLiveData<String> = MutableLiveData()


    private fun reset() {

        page = 0
        productListAdapter.reset()
        hasNextPage = true
        dataList.clear()
        productListAdapter.updateList()
        resetList.value = true
//        view.getScrollListener().resetState()
    }

    private var subscription: Disposable? = null

    private var categorySubscription :Disposable? = null

    init {
        reset()
        loadCategory()

        loadProduct()
    }


    fun loadCategory() {

        categorySubscription?.dispose()
        categorySubscription = categoryRepository
            .getCategories(userRepository.getUserToken())

            .doOnSubscribe { }
            .doOnTerminate {  }
            .subscribe { result ->
                if (result.isSuccess()) {
                    if ((result.data?.size ?: 0) > 0) {
                        categoryList.clear()
                        result.data?.let { categoryList.addAll(result.data) }
                        categoryListAdapter.notifyDataSetChanged()


                    }
                }
            }



    }

    fun loadProduct() {
        if (hasNextPage) {
            page++
            subscription?.dispose()
            subscription = productRepository
                .productSearch(userRepository.getUserToken(), query, page, null, null)

                .doOnSubscribe { onRetrieveProductListStart() }
                .doOnTerminate { onRetrieveProductListFinish() }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        if ((result.data?.results?.size ?: 0) > 0) {

                            emptyVisibility.value = View.GONE
                            onRetrieveProductListSuccess(
                                result.data?.results
                            )

                            if (result.data?.next == null) {
                                hasNextPage = false


                            }

                        } else {

                            emptyVisibility.value = View.VISIBLE




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
        retry.value = null
        if (page <= 1) {
            loading.value = true

            emptyVisibility.value = View.GONE

        }
        errorMessage.value = null
    }

    private fun onRetrieveProductListFinish() {
        loading.value = false
    }

    private fun onRetrieveProductListSuccess(productList: List<Product>?) {

        if (productList != null) {

            var insertIndex = 0
            if (dataList.size > 0)
                insertIndex = dataList.size

            dataList.addAll(productList)
            productListAdapter.notifyItemRangeInserted(insertIndex, productList.size)
        }

    }

    private fun onRetrieveProductListError(error: NetworkError?) {
        if (page <= 1)
            retry.value = error?.message
        else
            errorMessage.value = error?.message


    }

    fun onClickSearch() {

        searchValue.value = ""

    }


    override fun onCleared() {
        super.onCleared()
        categorySubscription?.dispose()
        subscription?.dispose()
    }

    fun loadMore() {
        loadProduct()

    }

    fun retry() {
        reset()
        loadProduct()
        loadCategory()

    }

    fun search(search: String?) {

        search?.let { getAnalytics()?.actionSearch(it) }

        reset()
        query = search
        loadProduct()

    }

}