package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class MapGeoCodeResponseModel(
    @SerializedName("results") val results: List<MapGeoCodeResultModel>?,
    @SerializedName("status") val status: String
) {

    data class MapGeoCodeResultModel(
        @SerializedName("address_components") val address_components: List<MapGeoCodeAddressModel>,
        @SerializedName("formatted_address") val formatted_address: String,
        @SerializedName("place_id") val place_id: String,
        @SerializedName("types") val types: List<String>
    )

    data class MapGeoCodeAddressModel(
        @SerializedName("long_name") val long_name: String,
        @SerializedName("short_name") val short_name: String,
        @SerializedName("types") val types: List<String>
    )
}