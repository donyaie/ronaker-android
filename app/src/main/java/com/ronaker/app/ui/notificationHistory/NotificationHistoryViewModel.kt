package com.ronaker.app.ui.notificationHistory


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.onesignal.OneSignalHelper
import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationHistoryViewModel @ViewModelInject constructor(
) : BaseViewModel() {


    val errorMessage: MutableLiveData<String> = MutableLiveData()


    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    var dataList = ArrayList<OneSignalHelper.Notifications>()

    val adapter = NotificationHistoryAdapter(dataList)


    private var subscription: Disposable? = null

    init {
        uiScope.launch {

            loadData()
        }
    }

    private suspend fun loadData() =
        withContext(Dispatchers.Unconfined) {


            val notifications = OneSignalHelper.getOneSignalNotifications()

            dataList.clear()

            dataList.addAll(notifications)

            MainScope().launch {

                adapter.notifyDataSetChanged()
            }

        }


    fun onRetry() {
        uiScope.launch {

            loadData()
        }

    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}