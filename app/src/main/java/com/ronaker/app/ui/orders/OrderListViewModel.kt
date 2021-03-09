package com.ronaker.app.ui.orders


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderRepository: OrderRepository
)  : BaseViewModel(),
    OrderItemAdapter.OrderItemListener {




    private var page = 0
    private var hasNextPage = true


    var dataList: ArrayList<Order> = ArrayList()



    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()



    val listView: MutableLiveData<List<Order>> = MutableLiveData()

    val launchOrderDetail: MutableLiveData<Order> = MutableLiveData()
    val launchOrderRateDetail: MutableLiveData<Order> = MutableLiveData()

    val resetList: MutableLiveData<Boolean> = MutableLiveData()

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()


    private var mFilter: String? = null
    private var subscription: Disposable? = null
    private var archiveSubscription: Disposable? = null

    fun getData() {

        uiScope.launch {
            loadData()
        }
    }

     fun reset() {

        page = 0
        hasNextPage = true
        dataList.clear()
//        productListAdapter.updateList(dataList)
        resetList.postValue(true)
    }

    suspend fun loadData() =
        withContext(Dispatchers.IO) {

            if (hasNextPage) {
                page++
                subscription?.dispose()

                subscription?.dispose()
                subscription = orderRepository
                    .getOrders( page, mFilter)

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

                            listView.postValue(dataList)

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
            loadData()
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



        listView.postValue(dataList)


    }

    override fun onClickItemRate(order: Order) {

        launchOrderRateDetail.postValue(order)
    }

    fun setFilter(filter: String?) {

        mFilter=filter
    }


}