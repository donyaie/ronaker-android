package com.ronaker.app.ui.orders


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import com.ronaker.app.model.toOrderList
import io.reactivex.disposables.Disposable
import java.util.logging.Filter
import javax.inject.Inject

class OrdersViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    @Inject
    lateinit
    var orderRepository: OrderRepository


    internal var page = 0
    internal var hasNextPage = true


    var dataList: ArrayList<Order>


    var productListAdapter: OrderItemAdapter
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val retry: MutableLiveData<Boolean> = MutableLiveData()
    val resetList: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    init {
        dataList = ArrayList()
        productListAdapter = OrderItemAdapter(dataList)


    }
    var mFilter:String?=null

    fun loadData(filter: String?) {

        mFilter=filter

        dataList.clear()
        subscription?.dispose()
        subscription = orderRepository
            .getOrders(userRepository.getUserToken(), filter)

            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    if ((result.data?.results?.size?:0) > 0) {


                        result.data?.results?.toOrderList()?.let { dataList.addAll(it) }
                        productListAdapter.updateproductList()

                        if (result.data?.next == null)
                            hasNextPage = false

                    } else {
                        hasNextPage = false
                    }
                } else {

                    errorMessage.value = result.error?.detail
                }
            }


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }



    fun retry() {
        loadData(mFilter)

    }


}