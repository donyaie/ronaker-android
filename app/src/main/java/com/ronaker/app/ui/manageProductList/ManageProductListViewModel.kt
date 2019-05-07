package com.ronaker.app.ui.manageProductList


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.model.toProduct
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ManageProductListViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    internal var page = 0
    internal var hasNextPage = true


    var dataList: ArrayList<Product>


    var productListAdapter: ManageProductAdapter
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val emptyView: MutableLiveData<Boolean> = MutableLiveData()


    private lateinit var subscription: Disposable

    init {
        dataList = ArrayList()
        productListAdapter = ManageProductAdapter(dataList)

        loadProduct()
    }

    fun loadProduct() {

        subscription = productRepository
            .getMyProduct(userRepository.getUserToken())

            .doOnSubscribe { onRetrieveProductListStart() }
            .doOnTerminate { onRetrieveProductListFinish() }
            .subscribe { result ->
                if (result.isSuccess()) {
                    if (result.data?.results?.size!! > 0) {


                        emptyView.value = false
                        onRetrieveProductListSuccess(
                            result.data?.results?.toProduct()
                        )
                    } else {
                        hasNextPage = false
                        emptyView.value = true
                    }
                } else {
                    onRetrieveProductListError(result.error)
                }
            }
    }


    private fun onRetrieveProductListStart() {

        if (page == 0) loading.value = true
        errorMessage.value = null
    }

    private fun onRetrieveProductListFinish() {
        loading.value = false
    }

    private fun onRetrieveProductListSuccess(productList: List<Product>?) {

        if (productList != null) {
            dataList.addAll(productList)
            productListAdapter.updateproductList()
        }

    }

    private fun onRetrieveProductListError(error: NetworkError?) {

        errorMessage.value = error?.detail

    }


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadMore() {
//        loadProduct()

    }

}