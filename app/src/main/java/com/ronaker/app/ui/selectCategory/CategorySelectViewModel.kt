package com.ronaker.app.ui.selectCategory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterCategorySelectBinding
import com.ronaker.app.model.Category


class CategorySelectViewModel {
    private val productTitle = MutableLiveData<String>()

    lateinit var data: Category
    lateinit var mParentViewModel: AddProductCategorySelectViewModel


    lateinit var mBinder: AdapterCategorySelectBinding

    fun bind(
        post: Category,
        binder: AdapterCategorySelectBinding,
        parentViewModel: AddProductCategorySelectViewModel
    ) {
        data = post

        mBinder = binder
        mParentViewModel = parentViewModel

        productTitle.value = data.title


    }


    fun onClickProduct() {

        mParentViewModel.selectCategory(data)


    }

    fun getProductTitle(): MutableLiveData<String> {
        return productTitle
    }


}