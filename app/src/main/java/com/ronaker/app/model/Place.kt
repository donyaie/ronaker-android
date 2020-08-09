package com.ronaker.app.model

import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel
import com.ronaker.app.data.network.response.GooglePlaceDetailResponseModel
import com.ronaker.app.data.network.response.MapGeoCodeResponseModel
import java.util.*
import kotlin.collections.ArrayList


data class Place(
    var placeId: String?,
    var description: String?,
    var mainText: String?,
    var secondaryText: String?,
    var latLng: LatLng? = null


)

fun List<GoogleAutocompleteResponseModel.GoogleAutocompletePredcationResponseModel>.toPlaceList(): List<Place> {
    val list: ArrayList<Place> = ArrayList()
    this.forEach {
        val product = Place(
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


    return Place(
        place_id,
        name,
        formatted_address,
        null,
        LatLng(geometry.location.lat, geometry.location.lng)


    )

}

fun MapGeoCodeResponseModel.converGeoToPlace(): Place? {


    var country: String? = null
    var city: String? = null
    var route: String? = null


    val stringBuilder = StringBuilder()

    val mapGeocode = this



    if (mapGeocode.status.toLowerCase(Locale.ROOT).compareTo("ok") == 0) {

        if (!mapGeocode.results.isNullOrEmpty()) {


            mapGeocode.results.forEach { Addresses ->


                for (mapGeoCodeAddress in Addresses.address_components) {

                    mapGeoCodeAddress.types.forEach { type ->
                        when {
                            type.contains("country", true) -> {

                                if (!mapGeoCodeAddress.long_name.contains("unnamed", true)) {

                                    country = mapGeoCodeAddress.long_name
                                }
                            }
                            type.contains("locality", true) -> {

                                if (!mapGeoCodeAddress.long_name.contains("unnamed", true)) {

                                    city = mapGeoCodeAddress.long_name
                                }

                            }
                            type.contains("route", true) -> {
                                if (!mapGeoCodeAddress.long_name.contains("unnamed", true)) {

                                    route = mapGeoCodeAddress.long_name
                                }
                            }
                        }

                    }


                }

            }

        }

    }

    if (!country.isNullOrEmpty()) {
        stringBuilder.append(country)
    }
    if (!city.isNullOrEmpty()) {
        stringBuilder.append(", ")
        stringBuilder.append(city)
    }
    if (!route.isNullOrEmpty()) {
        stringBuilder.append(", ")
        stringBuilder.append(route)
    }


    return this.results?.let {

        return Place(
            it[0].place_id,
            stringBuilder.toString(),
            stringBuilder.toString(),
            stringBuilder.toString(),
            null
        )

    }


}






