package com.ronaker.app.base

import androidx.lifecycle.ViewModel
import com.ronaker.app.injection.component.DaggerViewModelInjector
import com.ronaker.app.injection.component.ViewModelInjector
import com.ronaker.app.injection.module.NetworkModule
import com.ronaker.app.ui.post.PostListViewModel

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
        }
    }
}