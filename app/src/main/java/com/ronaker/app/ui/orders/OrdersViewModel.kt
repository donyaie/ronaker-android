package com.ronaker.app.ui.orders


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Order
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OrdersViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    internal var page = 0
    internal var hasNextPage = true


    var dataList: ArrayList<Order>


    var productListAdapter: OrderItemAdapter
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val resetList: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    init {
        dataList = ArrayList()
        productListAdapter = OrderItemAdapter(dataList)

        loadProduct()
    }

    fun loadProduct() {

//             subscription = productRepository
//                 .productSearch(userRepository.getUserToken(), page)
//
//                 .doOnSubscribe { loading.value=true }
//                 .doOnTerminate {loading.value=false}
//                 .subscribe { result ->
//                     if (result.isSuccess()) {
//                         if(result.data?.results?.size!! >0) {
//
//
//                             dataList.addAll(productList)
//                             productListAdapter.updateproductList()
//                         }else{
//                             hasNextPage = false
//                         }
//                     } else {
//                         onRetrieveProductListError(result.error)
//                     }
//                 }

        loading.value=false
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        dataList.add(Order("", "Macbook pro", Date(),Date(), 100.0, "/media/Screenshot_20190308-012950_RTeai7G.jpg"))
        productListAdapter.updateproductList()


    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun loadMore() {
        loadProduct()

    }

}