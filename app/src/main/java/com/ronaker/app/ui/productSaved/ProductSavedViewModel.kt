package com.ronaker.app.ui.productSaved


import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.ui.explore.ItemExploreAdapter
import com.ronaker.app.utils.actionSearch
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProductSavedViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    private var page = 0
    private var hasNextPage = true


    var dataList: ArrayList<Product> = ArrayList()


    private var query: String? = null


    var productListAdapter: ItemExploreAdapter = ItemExploreAdapter()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()


    private var subscription: Disposable? = null


    init {
        reset()

    }

    private fun reset() {

        page = 0
        hasNextPage = true
        dataList.clear()
//        productListAdapter.updateList(dataList)
        resetList.value = true
    }


    fun loadProduct() {
        if (hasNextPage) {
            page++
            subscription?.dispose()
            subscription = productRepository
                .productSearch( query, page, null, null, true)

                .doOnSubscribe { onRetrieveProductListStart() }
                .doOnTerminate { onRetrieveProductListFinish() }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        if ((result.data?.results?.size ?: 0) > 0) {

                            emptyVisibility.value = View.GONE
                            onRetrieveProductListSuccess(
                                result.data?.results
                            )

                            if (result.data?.next == null)
                                hasNextPage = false

                        } else {

                            emptyVisibility.value = View.VISIBLE

                            hasNextPage = false
                        }
                    } else {

                        onRetrieveProductListError(result.error)
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

            dataList.addAll(productList)
            productListAdapter.updateList(dataList)
        }

    }

    private fun onRetrieveProductListError(error: NetworkError?) {
        if (page <= 1)
            retry.value = error?.message
        else
            errorMessage.value = error?.message


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadMore() {
        loadProduct()

    }

    fun retry() {
        reset()
        loadProduct()

    }

    fun search(search: String?) {

        search?.let { getAnalytics()?.actionSearch(it) }

        reset()
        query = search
        loadProduct()

    }


}