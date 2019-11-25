package com.ronaker.app.data

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.data.network.GoogleMapApi
import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel
import com.ronaker.app.data.network.response.GooglePlaceDetailResponseModel
import com.ronaker.app.data.network.response.MapGeoCodeResponceModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GoogleMapRepository(private val api: GoogleMapApi,private val apiKey: String) {

    fun getQueryAutocomplete(Query: String ,latLng: LatLng?): Observable<GoogleAutocompleteResponseModel> {

        val language: String? = null// "en";

        val components: String? = null//"country:ir";
        val types = "geocode"
        val radius = "50000"

        val location = if (latLng == null) null else String.format("%s,%s", latLng.latitude, latLng.longitude)

        return api.getQueryAutocomplete(Query, location, radius, language, components, types, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }


    fun getPlaceDetails(placeId: String): Observable<GooglePlaceDetailResponseModel> {

        val language: String? = null// "en";


        return api.getPlaceDetails(placeId, language,apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }




    fun getGeocode(location: LatLng): Observable<MapGeoCodeResponceModel> {

        val language: String? = null// "en";


        val latlng = String.format("%s,%s", location.latitude, location.longitude)


        return api.getGeocode(null, latlng, null, null, language, null, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }



}

