package com.ronaker.app.model

import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel


data class Place(
    var placeId: String,
    var description: String,
    var mainText: String,
    var secondaryText: String
) {

}


fun List<GoogleAutocompleteResponseModel.GoogleAutocompletePredcationResponseModel>.toPlaceList(): List<Place> {
    var list: ArrayList<Place> = ArrayList()
    this.forEach {
        var product = Place(
            it.place_id,
            it.description,
            it.structured_formatting.main_text,
            it.structured_formatting.secondary_text
        )
        list.add(product)
    }

    return list

}



