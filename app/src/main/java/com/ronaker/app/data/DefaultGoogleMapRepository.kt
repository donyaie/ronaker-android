package com.ronaker.app.data

import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.network.GoogleMapApi
import com.ronaker.app.model.Place
import com.ronaker.app.model.converGeoToPlace
import com.ronaker.app.model.toPlace
import com.ronaker.app.model.toPlaceList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefaultGoogleMapRepository @Inject constructor(private val api: GoogleMapApi, private val resourcesRepository: ResourcesRepository) :
    GoogleMapRepository {

    override fun getQueryAutocomplete(Query: String, latLng: LatLng?): Observable<List<Place>?> {

        val language: String? = null// "en";

        val components: String? = null//"country:ir";
        val types = "(regions)"
        val radius = "50000"

        val location =
            if (latLng == null) null else String.format("%s,%s", latLng.latitude, latLng.longitude)

        return api.getQueryAutocomplete(
            Query,
            location,
            radius,
            language,
            components,
            types,
            resourcesRepository.getString(R.string.google_api_key_me)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.predictions?.toPlaceList()
            }

    }


    override fun getPlaceDetails(placeId: String): Observable<Place?> {

        val language: String? = null// "en";


        return api.getPlaceDetails(placeId, language, resourcesRepository.getString(R.string.google_api_key_me))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {

                it.result?.toPlace()
            }

    }


    override fun getGeocode(location: LatLng): Observable<Place?> {

        val language: String? = null// "en";


        val latlng = String.format("%s,%s", location.latitude, location.longitude)
        val types = "(regions)"

        return api.getGeocode(null, latlng, null, null, language, null, types, resourcesRepository.getString(R.string.google_api_key_me))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.converGeoToPlace()
            }

    }


}