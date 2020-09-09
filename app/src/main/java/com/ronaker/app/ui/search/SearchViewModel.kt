package com.ronaker.app.ui.search


import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.model.Product
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel @ViewModelInject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val analytics: FirebaseAnalytics
) : BaseViewModel() {


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val itemSearchVisibility: MutableLiveData<Int> = MutableLiveData()

    val itemRecentVisibility: MutableLiveData<Int> = MutableLiveData()


    var dataList: ArrayList<Product> = ArrayList()

    var listView: MutableLiveData<ArrayList<Product>> = MutableLiveData()


    lateinit var query: String


    private var itemSearchSubscription: Disposable? = null


    private var location: LatLng? = null

    suspend fun loadProduct(query: String) =

        withContext(Dispatchers.Unconfined) {


            itemSearchSubscription?.dispose()



            itemSearchSubscription = productRepository
                .productSearch(

                    query = query,
                    page = 1,
                    location = location,
                    radius = 50,
                    categorySiud = null
                )
                .doOnSubscribe {

                    errorMessage.postValue(null)
                }
                .doOnTerminate {

                    loading.postValue(false)
                }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        dataList.clear()
                        result.data?.results?.let { dataList.addAll(it) }

                        listView.postValue(dataList)


                    } else {

                        errorMessage.postValue(result.error?.message)

                    }
                }

        }


    override fun onCleared() {
        super.onCleared()
        itemSearchSubscription?.dispose()
    }



    fun search(query: String?) {
       if(query.isNullOrBlank()){
           itemSearchVisibility.postValue(View.GONE)
       }
        else{
           itemSearchVisibility.postValue(View.VISIBLE)
           uiScope.launch {

               loadProduct(query)
           }


       }
    }


}