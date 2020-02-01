package com.ronaker.app.ui.dialog

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterSelectBinding

class SelectAdapterViewModel (app: Application): BaseViewModel(app) {
    private val productTitle = MutableLiveData<String>()

    lateinit var data: SelectDialog.SelectItem
    lateinit var mParentViewModel: SelectDialogViewModel


    lateinit var mBinder: AdapterSelectBinding

    fun bind(
        post: SelectDialog.SelectItem,
        binder: AdapterSelectBinding,
        parentViewModel: SelectDialogViewModel
    ) {
        data = post

        mBinder = binder
        mParentViewModel = parentViewModel

        productTitle.value=data.title


    }


    fun onClickProduct() {

        mParentViewModel.selectCategory(data)


    }

    fun getTitle(): MutableLiveData<String> {
        return productTitle
    }


}