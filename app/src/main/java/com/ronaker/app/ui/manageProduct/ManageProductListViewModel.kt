package com.ronaker.app.ui.manageProduct


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.model.toProductList
import com.ronaker.app.ui.explore.ItemExploreAdapter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManageProductListViewModel (app: Application): BaseViewModel(app) {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    internal var page = 0
    internal var hasNextPage = true


    var dataList: ArrayList<Product> = ArrayList()


    var productListAdapter: ManageProductAdapter
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val emptyView: MutableLiveData<Boolean> = MutableLiveData()
    val addNewView: MutableLiveData<Boolean> = MutableLiveData()

    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    private  var subscription: Disposable?=null

    init {
        productListAdapter = ManageProductAdapter(dataList,this)
        reset()
    }



    internal fun reset() {

        page = 0
        hasNextPage = true
        dataList.clear()
        productListAdapter.updateproductList()
        resetList.value = true
//        view.getScrollListener().resetState()
    }

    fun loadProduct() {

        if (hasNextPage) {
            page++
            subscription?.dispose()
            subscription = productRepository
                .getMyProduct(userRepository.getUserToken(), page)

                .doOnSubscribe { onRetrieveProductListStart() }
                .doOnTerminate { onRetrieveProductListFinish() }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        if ((result.data?.results?.size?:0) > 0) {


                            addNewView.value = true
                            emptyView.value = false
                            onRetrieveProductListSuccess(
                                result.data?.results
                            )

                            if (result.data?.next == null) {
                                hasNextPage = false
                            }

                        } else {

                            addNewView.value = false
                            emptyView.value = true
                        }
                    } else {
                        onRetrieveProductListError(result.error)
                    }
                }
        }
    }


    private fun onRetrieveProductListStart() {
        retry.value = null
        if (page <=1 ) {
            loading.value = true

            addNewView.value = false
            emptyView.value = false

        }
        errorMessage.value = null
    }

    private fun onRetrieveProductListFinish() {
        loading.value = false


    }

    private fun onRetrieveProductListSuccess(productList: List<Product>?) {

//        if (productList != null) {
//            dataList.addAll(productList)
//            productListAdapter.updateproductList()
//        }


        if (productList != null) {

            var insertIndex=0
            if(dataList.size>0)
                insertIndex=dataList.size

            dataList.addAll(productList)
            productListAdapter.notifyItemRangeInserted(insertIndex,productList.size )
        }


    }

    private fun onRetrieveProductListError(error: NetworkError?) {

        if(page<=1)
            retry.value = error?.detail
        else
            errorMessage.value = error?.detail

    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadMore() {

        loadProduct()

    }

    fun retry(){

        reset()
        loadProduct()
    }

}