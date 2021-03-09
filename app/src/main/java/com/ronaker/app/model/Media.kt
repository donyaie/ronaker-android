package com.ronaker.app.model

import android.os.Parcelable
import com.ronaker.app.data.network.response.ContentImageResponseModel
import kotlinx.parcelize.Parcelize
@Parcelize
data class Media(
    var suid: String,
    var url: String,
    var created_at: String
) : Parcelable


enum class DocumentTypeEnum constructor(key: String, title: String) {
    IDCard("id_card", "ID Card"),
    Passport("passport", "Passport"),
    DrivingLicense("driving_license", "Driving License"),
    None("", "");


    var key: String = ""
        internal set


    var title: String = ""
        internal set

    init {
        this.key = key
        this.title = title
    }

    companion object {
        operator fun get(position: String): DocumentTypeEnum {
            var state = None
            for (stateEnum in values()) {
                if (position.compareTo(stateEnum.key, true) == 0)
                    state = stateEnum
            }
            return state
        }
    }

}


fun ContentImageResponseModel.toMediaModel(): Media {
    return Media(
        suid,
        content,
        created_at

    )

}
