package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class GooglePlaceDetailResponseModel(
    @SerializedName("result") var result: GooglePlaceResultResponseModel?,
    @SerializedName("status") var status: String
) {


    data class GooglePlaceResultResponseModel(
        @SerializedName("place_id") var place_id: String,
        @SerializedName("name") var name: String,
        @SerializedName("formatted_address") var formatted_address: String,
        @SerializedName("geometry") var geometry: GooglePlaceGeometryResponseModel
    )

    data class GooglePlaceGeometryResponseModel(
        @SerializedName("location") var location: GooglePlaceGeometryLocationResponseModel
    )


    data class GooglePlaceGeometryLocationResponseModel(
        @SerializedName("lat") var lat: Double,
        @SerializedName("lng") var lng: Double
    )


}


