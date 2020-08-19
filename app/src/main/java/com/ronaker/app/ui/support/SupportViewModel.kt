package com.ronaker.app.ui.support

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.model.Category
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class SupportViewModel(app: Application) : BaseViewModel(app) {

    internal val TAG = SupportViewModel::class.java.name


    val selectedPlace: MutableLiveData<Category> = MutableLiveData()



    init {

    }


}