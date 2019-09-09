package com.ronaker.app.model

import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel
import com.ronaker.app.data.network.response.GooglePlaceDetailResponseModel
import com.ronaker.app.data.network.response.MapGeoCodeResponceModel


data class Place(
    var placeId: String?,
    var description: String?,
    var mainText: String?,
    var secondaryText: String?,
    var lat: Double? = null,
    var lng: Double? = null
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

fun MapGeoCodeResponceModel.converGeoToPlace(): Place? {


//    val DefaultIndex = 0
//
//    var address: String? = null
//
//    val direction = java.util.ArrayList<LatLng>()
//
//    val add = java.util.ArrayList<MapGeoCodeResponceModel.MapGeoCodeAddressModel>()
//
//
//    val stringBuilder = StringBuilder()
//
//    var mapGeocode = this
//
//
//    if (mapGeocode != null)
//
//
//        if (mapGeocode.status.toLowerCase().compareTo("ok") == 0) {
//
//            if (mapGeocode?.results!!.isNotEmpty()) {
//
//
//                var neighborhood: String? = null
//
//                mapGeocode?.results?.forEach { Addresses ->
//
//
//                    for (mapGeoCodeAddress in Addresses.address_components) {
//
//                        mapGeoCodeAddress.types.forEach { type ->
//                            if (type.toLowerCase().trim({ it <= ' ' }).compareTo("neighborhood") == 0) {
//                                neighborhood = mapGeoCodeAddress.long_name
//
//                            }
//
//                        }
//
//                    }
//
//                }
//
//                var isNeighborhood = false
//
//
//                mapGeocode?.results?.get(DefaultIndex)
//                    ?.address_components?.forEach { mapGeoCodeAddress ->
//
//                    var remove = false
//
//                    for (type in mapGeoCodeAddress.types) {
//                        if (type.compareTo("locality") == 0 ||
//                            type.compareTo("administrative_area_level_1") == 0 ||
//                            type.compareTo("administrative_area_level_2") == 0 ||
//                            type.compareTo("sublocality_level_1") == 0 ||
//                            type.compareTo("country") == 0
//                        ) {
//                            remove = true
//                        } else if (type.toLowerCase().trim { it <= ' ' }.compareTo("neighborhood") == 0) {
//                            isNeighborhood = true
//                        }
//
//                    }
//
//
//                    if (!remove) {
//                        add.add(0, mapGeoCodeAddress)
//                    }
//
//
//                }
//
//                add.forEach { mapGeoCodeAddress ->
//
//                    stringBuilder.append(mapGeoCodeAddress.long_name).append(" , ")
//
//                }
//
//
//
//
//                if (add.size > 0)
//                    address = stringBuilder.substring(0, stringBuilder.length - 3)
//
//
//                if (!isNeighborhood && neighborhood != null)
//                    address = neighborhood + if (address == null) "" else " , $address"
//
//
//            }
//
//
//        }

    this.results?.let {

        return Place(
            it[0].place_id,
            it[0].formatted_address,
            it[0].formatted_address,
            it[0].formatted_address,
            null,
            null
        )

    }


    return null

}






