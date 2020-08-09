package com.ronaker.app.model

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    var url: String? = null,
    var suid: String? = null,
    var uri: Uri? = null,
    var isLocal: Boolean = false,
    var isSelected:Boolean=false
) : Parcelable {


    @IgnoredOnParcel
    val progress: MutableLiveData<Boolean> =
        MutableLiveData()



}