package com.ronaker.app.injection.component

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
import com.ronaker.app.ui.orders.OrderListViewModel
import com.ronaker.app.ui.orders.OrdersViewModel
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberViewModel
import com.ronaker.app.ui.profile.ProfileViewModel
import com.ronaker.app.ui.profileEdit.ProfileEditViewModel
import com.ronaker.app.ui.profileIdentify.ProfileIdentifyViewModel
import com.ronaker.app.ui.profilePayment.ProfilePaymentViewModel
import com.ronaker.app.ui.search.SearchViewModel
import com.ronaker.app.ui.splash.SplashViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(RepositoryModule::class)])
interface ViewModelInjector {
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
    fun inject(viewModel: SearchViewModel)
    fun inject(viewModel: OrderPreviewViewModel)
    fun inject(viewModel: AddProductLocationViewModel)
    fun inject(viewModel: AddProductLocationSearchViewModel)
    fun inject(viewModel: AddProductCategorySelectViewModel)
    fun inject(viewModel: ProfileEditViewModel)
    fun inject(viewModel: OrderListViewModel)

    fun inject(viewModel: ProfilePaymentViewModel)

    fun inject(viewModel: ProfileIdentifyViewModel)




    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun repositoryModule(repositoryModule: RepositoryModule): Builder
    }
}