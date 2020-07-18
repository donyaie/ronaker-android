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

    fun getData(filter: String?) {

        uiScope.launch {
            loadData(filter)
        }
    }

    suspend fun loadData(filter: String?) =
        withContext(Dispatchers.IO) {

            mFilter = filter

            subscription?.dispose()
            subscription = orderRepository
                .getOrders(userRepository.getUserToken(), filter)

                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                    retry.postValue(null)
                    loading.postValue(true)

                }
                .doOnTerminate {

                    loading.postValue(false)


                }
                .subscribe { result ->
                    if (result.isSuccess()) {


                        dataList.clear()
                        result.data?.results?.let { dataList.addAll(it) }
                        productListAdapter.updateList(dataList)

                       if( !result.data?.results.isNullOrEmpty()){
                           emptyVisibility.postValue(View.GONE)
                       }

                        if (result.data?.next == null)
                            hasNextPage = false

                        if(dataList.isEmpty())
                            emptyVisibility.postValue(View.VISIBLE)



                    } else {

                        retry.postValue(result.error?.message)
                    }
                }


        }


    suspend fun doArchive(suid: String) =
        withContext(Dispatchers.IO) {

            subscription?.dispose()
            subscription = orderRepository
                .updateOrderStatus(token =  userRepository.getUserToken(), suid = suid,isArchived = true)

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
    }


    fun retry() {

        uiScope.launch {
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