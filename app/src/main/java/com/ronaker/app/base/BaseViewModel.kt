package com.ronaker.app.base

import androidx.lifecycle.ViewModel
import com.ronaker.app.injection.component.DaggerViewModelInjector
import com.ronaker.app.injection.component.ViewModelInjector
import com.ronaker.app.injection.module.RepositoryModule
import com.ronaker.app.ui.addProduct.AddProductViewModel
import com.ronaker.app.ui.explore.ExploreViewModel
import com.ronaker.app.ui.login.LoginViewModel
import com.ronaker.app.ui.post.PostListViewModel
import com.ronaker.app.ui.splash.SplashViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .repositoryModule(RepositoryModule)
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
            is LoginViewModel -> injector.inject(this)

            is SplashViewModel -> injector.inject(this)
            is ExploreViewModel -> injector.inject(this)
            is AddProductViewModel -> injector.inject(this)
        }

    }
}