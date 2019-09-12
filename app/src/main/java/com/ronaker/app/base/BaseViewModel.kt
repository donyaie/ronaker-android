package com.ronaker.app.base

import androidx.lifecycle.ViewModel
import com.ronaker.app.injection.component.DaggerViewModelInjector
import com.ronaker.app.injection.component.ViewModelInjector
import com.ronaker.app.injection.module.RepositoryModule
import com.ronaker.app.ui.addProduct.*
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarViewModel
import com.ronaker.app.ui.explore.ExploreViewModel
import com.ronaker.app.ui.exploreProduct.ExploreProductViewModel
import com.ronaker.app.ui.inbox.InboxViewModel
import com.ronaker.app.ui.login.LoginViewModel
import com.ronaker.app.ui.manageProduct.ManageProductListViewModel
import com.ronaker.app.ui.manageProduct.ManageProductViewModel
import com.ronaker.app.ui.orderMessage.OrderMessageViewModel
import com.ronaker.app.ui.orderPreview.OrderPreviewViewModel
import com.ronaker.app.ui.orders.OrdersViewModel
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberViewModel
import com.ronaker.app.ui.profile.ProfileViewModel
import com.ronaker.app.ui.search.SearchViewModel
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
            is LoginViewModel -> injector.inject(this)
            is SplashViewModel -> injector.inject(this)
            is ExploreViewModel -> injector.inject(this)
            is AddProductViewModel -> injector.inject(this)
            is AddProductImageAdapterViewModel->injector.inject(this)
            is PhoneNumberViewModel->injector.inject(this)
            is ExploreProductViewModel ->injector.inject(this)
            is ManageProductListViewModel ->injector.inject(this)
            is ManageProductViewModel->injector.inject(this)



            is CheckoutCalendarViewModel->injector.inject(this)
            is OrdersViewModel ->injector.inject(this)
            is OrderMessageViewModel->injector.inject(this)
            is ProfileViewModel->injector.inject(this)
            is InboxViewModel ->injector.inject(this)
            is SearchViewModel ->injector.inject(this)

            is OrderPreviewViewModel->injector.inject(this)


            is AddProductLocationViewModel->injector.inject(this)
            is AddProductLocationSearchViewModel->injector.inject(this)
            is AddProductCategorySelectViewModel->injector.inject(this)
        }

    }
}