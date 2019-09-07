package com.ronaker.app.data.network.response

import com.google.gson.annotations.SerializedName

data class GoogleAutocompleteResponseModel(
    @SerializedName("predictions")  var predictions: List<GoogleAutocompletePredcationResponseModel>?,
    @SerializedName("status")  var status: String
) {


    data class GoogleAutocompletePredcationResponseModel(
        @SerializedName("description")   var description: String,


        @SerializedName("id")  var id: String,
        @SerializedName("place_id")  var place_id: String,
        @SerializedName("reference") var reference: String,
        @SerializedName("structured_formatting")  var structured_formatting: GoogleAutocompleteStructureResponseModel
    )

    data class GoogleAutocompleteStructureResponseModel(
        @SerializedName("main_text")  var main_text: String,
        @SerializedName("secondary_text")  var secondary_text: String
    )

}


