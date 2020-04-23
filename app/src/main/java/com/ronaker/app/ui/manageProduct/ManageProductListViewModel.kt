package com.ronaker.app.ui.manageProduct


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageProductListViewModel(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    private var page = 0
    private var hasNextPage = true


    var dataList: ArrayList<Product> = ArrayList()


    var productListAdapter: ManageProductAdapter = ManageProductAdapter()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val emptyView: MutableLiveData<Boolean> = MutableLiveData()
    val addNewView: MutableLiveData<Boolean> = MutableLiveData()

    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    private var subscription: Disposable? = null

    init {
        reset()
    }


    private fun reset() {

        page = 0
        hasNextPage = true
        dataList.clear()

        resetList.postValue(true)
//        view.getScrollListener().resetState()
    }

    suspend fun loadProduct() =
        withContext(Dispatchers.IO) {

            if (hasNextPage) {
                page++
                subscription?.dispose()
                subscription = productRepository
                    .getMyProduct(userRepository.getUserToken(), page)

                    .doOnSubscribe {
                        retry.postValue(null)
                        if (page <= 1) {
                            loading.postValue(true)

//                            addNewView.postValue(false)
//                            emptyView.postValue(false)

                        }
                        errorMessage.postValue(null)
                    }
                    .doOnTerminate { loading.postValue(false) }
                    .subscribe { result ->
                        if (result.isSuccess()) {


                            result.data?.results?.let { dataList.addAll(it) }
                            productListAdapter.updateList(dataList)


                            if (result.data?.next == null) {
                                hasNextPage = false
                            }

                            if (!result.data?.results.isNullOrEmpty()) {


                                addNewView.postValue(true)
                                emptyView.postValue(false)
                            }


                            if (dataList.isEmpty()) {

                                addNewView.postValue(false)
                                emptyView.postValue(true)
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

}