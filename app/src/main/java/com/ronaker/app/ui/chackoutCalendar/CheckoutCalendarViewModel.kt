package com.ronaker.app.ui.chackoutCalendar


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CheckoutCalendarViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()



    lateinit var suid: String


    private var subscription: Disposable? = null

    init {

    }

    fun loadProduct(suid: String) {

        this.suid = suid


    }



    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}