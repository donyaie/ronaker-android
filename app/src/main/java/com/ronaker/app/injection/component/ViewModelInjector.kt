package com.ronaker.app.injection.component

import com.ronaker.app.injection.module.RepositoryModule
import com.ronaker.app.ui.addProduct.*
import com.ronaker.app.ui.dashboard.DashboardViewModel
import com.ronaker.app.ui.explore.ExploreViewModel
import com.ronaker.app.ui.exploreProduct.ExploreProductViewModel
import com.ronaker.app.ui.inbox.InboxViewModel
import com.ronaker.app.ui.login.LoginViewModel
import com.ronaker.app.ui.manageProduct.ManageProductListViewModel
import com.ronaker.app.ui.manageProduct.ManageProductViewModel
import com.ronaker.app.ui.orderAcceptIntro.OrderAcceptViewModel
import com.ronaker.app.ui.orderCancel.OrderCancelViewModel
import com.ronaker.app.ui.orderCreate.OrderCreateViewModel
import com.ronaker.app.ui.orderDecline.OrderDeclineViewModel
import com.ronaker.app.ui.orderFinish.OrderFinishViewModel
import com.ronaker.app.ui.orderPreview.OrderPreviewViewModel
import com.ronaker.app.ui.orderStartRenting.OrderStartRentingViewModel
import com.ronaker.app.ui.orders.OrderListViewModel
import com.ronaker.app.ui.orders.OrdersViewModel
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberViewModel
import com.ronaker.app.ui.productRate.ProductRateViewModel
import com.ronaker.app.ui.productSaved.ProductSavedViewModel
import com.ronaker.app.ui.profile.ProfileViewModel
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteViewModel
import com.ronaker.app.ui.profileEdit.ProfileEditViewModel
import com.ronaker.app.ui.profileEmailEdit.ProfileEmailEditViewModel
import com.ronaker.app.ui.profileEmailVerify.ProfileEmailVerifyViewModel
import com.ronaker.app.ui.profileIdentify.ProfileIdentifyViewModel
import com.ronaker.app.ui.profileImage.ProfileImageViewModel
import com.ronaker.app.ui.profileNameEdit.ProfileNameEditViewModel
import com.ronaker.app.ui.profilePayment.ProfilePaymentViewModel
import com.ronaker.app.ui.profilePaymentHistoryList.ProfilePaymentHistoryListViewModel
import com.ronaker.app.ui.profilePaymentList.ProfilePaymentListViewModel
import com.ronaker.app.ui.profileSetting.ProfileSettingViewModel
import com.ronaker.app.ui.search.SearchViewModel
import com.ronaker.app.ui.searchLocationDialog.AddProductLocationSearchViewModel
import com.ronaker.app.ui.selectCategory.AddProductCategorySelectViewModel
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
    fun inject(viewModel: OrdersViewModel)
    fun inject(viewModel: ProfileViewModel)
    fun inject(viewModel: InboxViewModel)
    fun inject(viewModel: SearchViewModel)
    fun inject(viewModel: OrderPreviewViewModel)
    fun inject(viewModel: AddProductLocationViewModel)
    fun inject(viewModel: AddProductLocationSearchViewModel)
    fun inject(viewModel: AddProductCategorySelectViewModel)
    fun inject(viewModel: ProfileCompleteViewModel)
    fun inject(viewModel: OrderListViewModel)
    fun inject(viewModel: ProfilePaymentViewModel)
    fun inject(viewModel: ProfileIdentifyViewModel)
    fun inject(viewModel: DashboardViewModel)
    fun inject(viewModel: OrderAcceptViewModel)
    fun inject(viewModel: OrderDeclineViewModel)
    fun inject(viewModel: OrderStartRentingViewModel)
    fun inject(viewModel: OrderFinishViewModel)
    fun inject(viewModel: ProfileImageViewModel)
    fun inject(viewModel: ProfileSettingViewModel)
    fun inject(viewModel: ProductRateViewModel)
    fun inject(viewModel: OrderCancelViewModel)
    fun inject(viewModel: ProductSavedViewModel)
    fun inject(viewModel: ProfileEmailVerifyViewModel)
    fun inject(viewModel: ProfileEditViewModel)
    fun inject(viewModel: ProfileNameEditViewModel)
    fun inject(viewModel: ProfileEmailEditViewModel)
    fun inject(viewModel: ProfilePaymentListViewModel)
    fun inject(viewModel: ProfilePaymentHistoryListViewModel)
    fun inject(viewModel: OrderCreateViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun repositoryModule(repositoryModule: RepositoryModule): Builder
    }
}