package com.ronaker.app.data.network

import com.ronaker.app.data.network.response.GoogleAutocompleteResponseModel
import com.ronaker.app.data.network.response.GooglePlaceDetailResponseModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */
interface GoogleMapApi {


    @GET("/maps/api/place/autocomplete/json?")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getQueryAutocomplete(
        @Query("input") input: String,
        @Query("location") location: String?,
        @Query("radius") radius: String,
        @Query("language") language: String?,
        @Query("components") components: String?,
        @Query("types") types: String,
        @Query("key") Key: String
    ): Observable<GoogleAutocompleteResponseModel>




    @GET("/maps/api/place/details/json?")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getPlaceDetails(
        @Query("placeid") placeId: String,
        @Query("language") language: String?,
        @Query("key") Key: String
    ): Observable<GooglePlaceDetailResponseModel>





}