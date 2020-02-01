package com.ronaker.app.ui.dialog

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Category
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class SelectDialogViewModel(app:Application) : BaseViewModel(app) {

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

    override fun onCleared() {
        super.onCleared()

    }

    fun searchLocation( items: List< SelectDialog.SelectItem>) {

        dataList.clear()
        dataList.addAll(items)
        listAdapter.notifyDataSetChanged()



    }

    fun selectCategory(data: SelectDialog.SelectItem) {
        selectedPlace.value = data

    }

}