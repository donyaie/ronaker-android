package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.ContentImageResponceModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    var suid: String,
    var url: String,
    var created_at: String
): Parcelable

fun ContentImageResponceModel.toMediaModel(): Media {
    return Media(
        suid,
        content,
        created_at

    )

}
