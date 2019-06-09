package com.ronaker.app.injection.component

import com.ronaker.app.injection.module.RepositoryModule
import com.ronaker.app.ui.addProduct.AddProductImageAdapterViewModel
import com.ronaker.app.ui.addProduct.AddProductViewModel
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarViewModel
import com.ronaker.app.ui.explore.ExploreViewModel
import com.ronaker.app.ui.exploreProduct.ExploreProductViewModel
import com.ronaker.app.ui.inbox.InboxViewModel
import com.ronaker.app.ui.login.LoginViewModel
import com.ronaker.app.ui.manageProduct.ManageProductListViewModel
import com.ronaker.app.ui.manageProduct.ManageProductViewModel
import com.ronaker.app.ui.orderMessage.OrderMessageViewModel
import com.ronaker.app.ui.orders.OrdersViewModel
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberViewModel
import com.ronaker.app.ui.post.PostListViewModel
import com.ronaker.app.ui.profile.ProfileViewModel
import com.ronaker.app.ui.splash.SplashViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(RepositoryModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param viewModel ViewModel in which to inject the dependencies
     */
    fun inject(viewModel: PostListViewModel)

    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: SplashViewModel)
    fun inject(viewModel: ExploreViewModel)
    fun inject(viewModel: AddProductViewModel)
    fun inject(viewModel: AddProductImageAdapterViewModel)
    fun inject(viewModel: PhoneNumberViewModel)
    fun inject(viewModel: ExploreProductViewModel)
    fun inject(viewModel: ManageProductListViewModel)
    fun inject(viewModel: ManageProductViewModel)
    fun inject(viewModel: CheckoutCalendarViewModel)
    fun inject(viewModel: OrdersViewModel)
    fun inject(viewModel: OrderMessageViewModel)
    fun inject(viewModel: ProfileViewModel)
    fun inject(viewModel: InboxViewModel)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun repositoryModule(repositoryModule: RepositoryModule): Builder
    }
}