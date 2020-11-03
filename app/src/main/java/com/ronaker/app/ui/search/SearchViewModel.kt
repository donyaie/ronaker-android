package com.ronaker.app.ui.search


import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.model.Category
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

    val emptyVisibility: MutableLiveData<Int> = MutableLiveData()


    var listView: MutableLiveData<ArrayList<Any>> = MutableLiveData()


    lateinit var query: String


    private var itemSearchSubscription: Disposable? = null


    private var location: LatLng? = null


    private var tempCategory: ArrayList<Category> = ArrayList()

    init {

        categoryRepository.getCategoriesLocal()?.forEach {

            tempCategory.add(it)
            it.sub_categories?.forEach {sub->

                tempCategory.add(sub.apply { this.title= "${sub.title} in ${it.title}"})


            }

        }

    }


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


                       fillSearch(result.data?.results, query)




                    } else {
                        fillSearch(null, query)
                        errorMessage.postValue(result.error?.message)


                    }
                }

        }


    private fun fillSearch(itemList: List<Product>?, query: String) {
        val result = ArrayList<Any>()

        result.addAll(tempCategory.filter {  it.title?.contains(query, true)  == true })



        itemList?.let { result.addAll(it) }



        if(result.isNullOrEmpty()){

            itemSearchVisibility.postValue(View.GONE)
            itemRecentVisibility.postValue(View.GONE)
            emptyVisibility.postValue(View.VISIBLE)
        }else{

            itemSearchVisibility.postValue(View.VISIBLE)
            itemRecentVisibility.postValue(View.GONE)
            emptyVisibility.postValue(View.GONE)
        }


        listView.postValue(result)
    }


    override fun onCleared() {
        super.onCleared()
        itemSearchSubscription?.dispose()
    }


    fun search(query: String?) {
        if (query.isNullOrBlank()) {

            itemSearchVisibility.postValue(View.GONE)
            itemRecentVisibility.postValue(View.VISIBLE)
            emptyVisibility.postValue(View.GONE)

        } else {
            uiScope.launch {

                loadProduct(query)
            }


        }
    }


}