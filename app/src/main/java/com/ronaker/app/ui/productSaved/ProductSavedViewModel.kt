package com.ronaker.app.ui.productSaved


import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.actionSearch
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductSavedViewModel @ViewModelInject constructor(
    private val productRepository: ProductRepository,
    private val analytics: FirebaseAnalytics
)  : BaseViewModel() {




    private var page = 1
    private var hasNextPage = true


    var dataList: ArrayList<Product> = ArrayList()


    val listView: MutableLiveData<ArrayList<Product>> = MutableLiveData()


    private var query: String? = null

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

        page = 1
        hasNextPage = true
        dataList.clear()
//        productListAdapter.updateList(dataList)
        resetList.postValue(true)
    }


    suspend fun loadProduct() =

        withContext(Dispatchers.Unconfined) {
            if (hasNextPage) {

                subscription?.dispose()
                subscription = productRepository
                    .productSearch(query, page, null, null, true)

                    .doOnSubscribe { onRetrieveProductListStart() }
                    .doOnTerminate { onRetrieveProductListFinish() }
                    .subscribe { result ->
                        if (result.isSuccess()) {

                            page++
                            if ((result.data?.results?.size ?: 0) > 0) {

                                emptyVisibility.postValue(View.GONE)
                                onRetrieveProductListSuccess(
                                    result.data?.results
                                )

                                if (result.data?.next == null)
                                    hasNextPage = false

                            } else {

                                emptyVisibility.postValue(View.VISIBLE)

                                hasNextPage = false
                            }
                        } else {

                            onRetrieveProductListError(result.error)
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

            dataList.addAll(productList)
            listView.postValue(dataList)
        }

    }

    private fun onRetrieveProductListError(error: NetworkError?) {
        if (page <= 1)
            retry.postValue(error?.message)
        else
            errorMessage.postValue(error?.message)


    }


    override fun onCleared() {
        super.onCleared()
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
        }

    }

    fun search(search: String?) {

        search?.let { analytics.actionSearch(it) }

        reset()
        query = search
        uiScope.launch {

            loadProduct()
        }
    }


}