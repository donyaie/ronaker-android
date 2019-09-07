package com.ronaker.app.model

import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel
import com.ronaker.app.data.network.response.GooglePlaceDetailResponseModel


data class Place(
    var placeId: String?,
    var description: String?,
    var mainText: String?,
    var secondaryText: String?,
    var lat:Double?=null,
    var lng:Double?=null
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


fun GooglePlaceDetailResponseModel.GooglePlaceResultResponseModel.toPlace(): Place {

        var product = Place(
            this.place_id,
            this.name,
            this.formatted_address,
            null,
            this.geometry.location.lat,
            this.geometry.location.lng
        )


    return product

}



