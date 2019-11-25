package com.ronaker.app.data

import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel
import com.ronaker.app.data.network.response.GooglePlaceDetailResponseModel
import com.ronaker.app.data.network.response.MapGeoCodeResponceModel
import io.reactivex.Observable

interface GoogleMapRepository {
    fun getQueryAutocomplete(Query: String, latLng: LatLng?): Observable<GoogleAutocompleteResponseModel>
    fun getPlaceDetails(placeId: String): Observable<GooglePlaceDetailResponseModel>
    fun getGeocode(location: LatLng): Observable<MapGeoCodeResponceModel>
}

