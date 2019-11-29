package com.ronaker.app.data

import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.model.Place
import io.reactivex.Observable

interface GoogleMapRepository {
    fun getQueryAutocomplete(Query: String, latLng: LatLng?): Observable<List<Place>?>
    fun getPlaceDetails(placeId: String): Observable<Place?>
    fun getGeocode(location: LatLng): Observable<Place?>
}

