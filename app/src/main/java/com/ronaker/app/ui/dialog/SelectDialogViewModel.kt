package com.ronaker.app.ui.dialog

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import javax.inject.Inject


class SelectDialogViewModel(app: Application) : BaseViewModel(app) {

    internal val TAG = SelectDialogViewModel::class.java.name


    @Inject
    lateinit var context: Context


    val title: MutableLiveData<String> = MutableLiveData()


    val selectedPlace: MutableLiveData<SelectDialog.SelectItem> = MutableLiveData()


    var dataList: ArrayList<SelectDialog.SelectItem> = ArrayList()
    var listAdapter: SelectAdapter


    init {

        listAdapter = SelectAdapter(dataList, this)

    }

    fun searchLocation(items: List<SelectDialog.SelectItem>) {

        dataList.clear()
        dataList.addAll(items)
        listAdapter.notifyDataSetChanged()


    }

    fun selectCategory(data: SelectDialog.SelectItem) {
        selectedPlace.value = data

    }

}