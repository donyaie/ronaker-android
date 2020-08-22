package com.ronaker.app.ui.notificationHistory

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.onesignal.OneSignalHelper

class NotificationHistoryAdapterViewModel{
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()



    fun bind(
        data: OneSignalHelper.Notifications
    ) {
        description.postValue(data.MESSAGE)
        title.postValue(data.TITLE)
    }


}