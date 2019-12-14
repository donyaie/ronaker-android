package com.ronaker.app.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.General
import com.ronaker.app.injection.component.DaggerViewModelInjector
import com.ronaker.app.injection.component.ViewModelInjector
import com.ronaker.app.injection.module.RepositoryModule
import com.ronaker.app.ui.addProduct.*
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarViewModel
import com.ronaker.app.ui.dashboard.DashboardViewModel
import com.ronaker.app.ui.explore.ExploreViewModel
import com.ronaker.app.ui.exploreProduct.ExploreProductViewModel
import com.ronaker.app.ui.inbox.InboxViewModel
import com.ronaker.app.ui.login.LoginViewModel
import com.ronaker.app.ui.manageProduct.ManageProductListViewModel
import com.ronaker.app.ui.manageProduct.ManageProductViewModel
import com.ronaker.app.ui.orderAcceptIntro.OrderAcceptViewModel
import com.ronaker.app.ui.orderCancel.OrderCancelViewModel
import com.ronaker.app.ui.orderDecline.OrderDeclineViewModel
import com.ronaker.app.ui.orderFinish.OrderFinishViewModel
import com.ronaker.app.ui.orderMessage.OrderMessageViewModel
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
import com.ronaker.app.ui.profileSetting.ProfileSettingViewModel
import com.ronaker.app.ui.search.SearchViewModel
import com.ronaker.app.ui.splash.SplashViewModel

abstract class BaseViewModel(private val app: Application) : AndroidViewModel(app) {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .repositoryModule(RepositoryModule(app))
        .build()


    fun getAnalytics(): FirebaseAnalytics? {

        return if (app is General)
            app.analytics
        else
            null
    }

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
            is AddProductImageAdapterViewModel -> injector.inject(this)
            is PhoneNumberViewModel -> injector.inject(this)
            is ExploreProductViewModel -> injector.inject(this)
            is ManageProductListViewModel -> injector.inject(this)
            is ManageProductViewModel -> injector.inject(this)
            is OrderListViewModel -> injector.inject(this)
            is CheckoutCalendarViewModel -> injector.inject(this)
            is OrdersViewModel -> injector.inject(this)
            is OrderMessageViewModel -> injector.inject(this)
            is ProfileViewModel -> injector.inject(this)
            is InboxViewModel -> injector.inject(this)
            is SearchViewModel -> injector.inject(this)

            is OrderPreviewViewModel -> injector.inject(this)


            is AddProductLocationViewModel -> injector.inject(this)
            is AddProductLocationSearchViewModel -> injector.inject(this)
            is AddProductCategorySelectViewModel -> injector.inject(this)
            is ProfileCompleteViewModel -> injector.inject(this)

            is ProfilePaymentViewModel -> injector.inject(this)
            is ProfileIdentifyViewModel -> injector.inject(this)

            is DashboardViewModel -> injector.inject(this)

            is OrderAcceptViewModel -> injector.inject(this)

            is OrderDeclineViewModel -> injector.inject(this)
            is OrderStartRentingViewModel -> injector.inject(this)
            is OrderFinishViewModel -> injector.inject(this)
            is ProfileImageViewModel -> injector.inject(this)
            is ProfileSettingViewModel -> injector.inject(this)
            is ProductRateViewModel -> injector.inject(this)

            is OrderCancelViewModel -> injector.inject(this)
            is ProductSavedViewModel -> injector.inject(this)
            is ProfileEmailVerifyViewModel -> injector.inject(this)
            is ProfileEditViewModel -> injector.inject(this)
            is ProfileNameEditViewModel -> injector.inject(this)
            is ProfileEmailEditViewModel -> injector.inject(this)
        }

    }
}