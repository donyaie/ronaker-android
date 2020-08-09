package com.ronaker.app.ui.orders


import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderListViewModel(app: Application) : BaseViewModel(app),
    OrderItemAdapter.OrderItemListener {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    @Inject
    lateinit
    var orderRepository: OrderRepository


    private var page = 0
    private var hasNextPage = true


    var dataList: ArrayList<Order> = ArrayList()


    var productListAdapter: OrderItemAdapter = OrderItemAdapter(this)

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()


    val launchOrderDetail: MutableLiveData<Order> = MutableLiveData()
    val launchOrderRateDetail: MutableLiveData<Order> = MutableLiveData()

    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()


    private var mFilter: String? = null
    private var subscription: Disposable? = null
    private var archiveSubscription: Disposable? = null

    fun getData(filter: String?) {

        uiScope.launch {
            loadData(filter)
        }
    }

     fun reset() {

        page = 0
        hasNextPage = true
        dataList.clear()
//        productListAdapter.updateList(dataList)
        resetList.postValue(true)
    }

    suspend fun loadData(filter: String?) =
        withContext(Dispatchers.IO) {

            if (hasNextPage) {
                page++
                subscription?.dispose()

                mFilter = filter

                subscription?.dispose()
                subscription = orderRepository
                    .getOrders( page, filter)

                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnSubscribe {

                        retry.postValue(null)
                        if (page <= 1) {
                            loading.postValue(true)

                            emptyVisibility.postValue(View.GONE)

                        }
                        errorMessage.postValue(null)

//
//
//                        retry.postValue(null)
//                        loading.postValue(true)

                    }
                    .doOnTerminate {

                        loading.postValue(false)


                    }
                    .subscribe { result ->
                        if (result.isSuccess()) {


//                            dataList.clear()
                            result.data?.results?.let { dataList.addAll(it) }
                            productListAdapter.updateList(dataList)

                            if (!result.data?.results.isNullOrEmpty()) {
                                emptyVisibility.postValue(View.GONE)
                            }

                            if (result.data?.next == null)
                                hasNextPage = false

                            if (dataList.isEmpty())
                                emptyVisibility.postValue(View.VISIBLE)







                        } else {

                            if (page <= 1)
                                retry.postValue(result.error?.message)
                            else
                                errorMessage.postValue(result.error?.message)

                        }
                    }
            }

        }


    suspend fun doArchive(suid: String) =
        withContext(Dispatchers.IO) {

            archiveSubscription?.dispose()
            archiveSubscription = orderRepository
                .updateOrderStatus(
                    suid = suid,
                    isArchived = true
                )

                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                }
                .doOnTerminate {
                }
                .subscribe { result ->
                    if (!result.isAcceptable()) {


                        errorMessage.postValue(result.error?.message)


                    }
                }


        }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
        archiveSubscription?.dispose()
    }


    fun retry() {

        uiScope.launch {
            reset()
            loadData(mFilter)
        }

    }

    override fun onClickItem(order: Order) {

        launchOrderDetail.postValue(order)
    }

    override fun onClickItemArchive(order: Order) {

        uiScope.launch {
            doArchive(order.suid)
        }

        dataList.remove(order)
        productListAdapter.updateList(dataList)


    }

    override fun onClickItemRate(order: Order) {

        launchOrderRateDetail.postValue(order)
    }


}