package com.ronaker.app.ui.dialog

import androidx.lifecycle.MutableLiveData

class SelectDialogViewModel {

    internal val TAG = SelectDialogViewModel::class.java.name




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