package com.ronaker.app.ui.explore


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.model.toProductList
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ExploreViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    internal var page = 0
    internal var hasNextPage = true


    var dataList: ArrayList<Product> = ArrayList()


    var productListAdapter: ItemExploreAdapter
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val resetList: MutableLiveData<Boolean> = MutableLiveData()


    val searchValue: MutableLiveData<String> = MutableLiveData()


    internal fun reset() {

        page = 0
        hasNextPage = true
        dataList.clear()
        productListAdapter.updateproductList()
        resetList.value = true
//        view.getScrollListener().resetState()
    }

    private  var subscription: Disposable?=null

    init {
        productListAdapter = ItemExploreAdapter(dataList)
        reset()

        loadProduct()
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

                            onRetrieveProductListSuccess(
                                result.data?.results?.toProductList()
                            )

                            if (result.data?.next == null)
                                hasNextPage = false

                        } else {
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
            retry.value =  error?.detail
        else
            errorMessage.value = error?.detail


    }

    fun onClickSearch() {

        searchValue.value = ""

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


    var query: String? = null

    fun search(search: String?) {

        reset()
        query = search
        loadProduct()

    }

}