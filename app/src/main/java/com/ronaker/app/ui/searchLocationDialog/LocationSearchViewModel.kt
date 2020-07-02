package com.ronaker.app.ui.searchLocationDialog

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterLocationSearchBinding
import com.ronaker.app.model.Place
import com.ronaker.app.ui.searchLocationDialog.AddProductLocationSearchViewModel


class LocationSearchViewModel (app: Application): BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()

    lateinit var data: Place
    lateinit var mParentViewModel: AddProductLocationSearchViewModel


    lateinit var mBinder: AdapterLocationSearchBinding

    fun bind(
        post: Place,
        binder: AdapterLocationSearchBinding,
        parentViewModel: AddProductLocationSearchViewModel
    ) {
        data = post

        mBinder = binder
        mParentViewModel = parentViewModel

        productTitle.value="${data.mainText} - ${data.secondaryText} "


    }


    fun onClickProduct() {
        mParentViewModel.selectPlace(data)


    }

    fun getProductTitle(): MutableLiveData<String> {
        return productTitle
    }


}