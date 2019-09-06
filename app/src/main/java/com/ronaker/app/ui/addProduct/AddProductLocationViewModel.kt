package com.ronaker.app.ui.addProduct

import android.content.Context
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductLocationViewModel : BaseViewModel() {

    internal val TAG = AddProductLocationViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    @Inject
    lateinit var context: Context

    @Inject
    lateinit var productRepository: ProductRepository


    private var getproductSubscription: Disposable? = null
    private var updateproductSubscription: Disposable? = null




    init {



    }

    override fun onCleared() {
        super.onCleared()
        getproductSubscription?.dispose()
        updateproductSubscription?.dispose()

    }

}