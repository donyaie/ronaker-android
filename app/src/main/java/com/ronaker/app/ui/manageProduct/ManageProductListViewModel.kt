package com.ronaker.app.ui.manageProduct


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ManageProductListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : BaseViewModel() {


    private var page = 1
    private var hasNextPage = true


    var dataList: ArrayList<Product> = ArrayList()

    var listView: MutableLiveData<ArrayList<Product>> = MutableLiveData()


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val emptyView: MutableLiveData<Boolean> = MutableLiveData()
    val addNewView: MutableLiveData<Boolean> = MutableLiveData()
    val addNewProduct: MutableLiveData<Boolean> = MutableLiveData()
    val completeProfile: MutableLiveData<String> = MutableLiveData()

    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    private var subscription: Disposable? = null
    private var stripeSubscription: Disposable? = null

    init {
        reset()
    }


    private fun reset() {

        page = 1
        hasNextPage = true
        dataList.clear()

        resetList.postValue(true)
//        view.getScrollListener().resetState()
    }

    suspend fun loadProduct() =
        withContext(Dispatchers.IO) {

            if (hasNextPage) {

                subscription?.dispose()
                subscription = productRepository
                    .getMyProduct(page)

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
                            page++

                            result.data?.results?.let { dataList.addAll(it) }
                            listView.postValue(dataList)


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


                            if (hasNextPage && page == 2) {
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


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        stripeSubscription?.dispose()
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

    fun checkIsComplete() {

        uiScope.launch {
            // TODO Strip
//            checkStripe()

            if( userRepository.getUserInfo()?.isComplete() == true)
            {
                addNewProduct.postValue(true)
            }else
                completeProfile.postValue(null )


        }

    }


    suspend fun checkStripe() =
        withContext(Dispatchers.IO) {

            stripeSubscription?.dispose()
            stripeSubscription = userRepository
                .stripeSetup()

                .doOnSubscribe {

                }
                .doOnTerminate { }
                .subscribe { result ->
                    if (result.isSuccess()) {

                       if( result.data?.is_ready ==true && userRepository.getUserInfo()?.isComplete() == true)
                       {
                           addNewProduct.postValue(true)
                       }else
                           completeProfile.postValue(result.data?.link )
                    } else {
                        errorMessage.postValue(result.error?.message)
                    }
                }
        }


}