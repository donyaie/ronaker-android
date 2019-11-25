package com.ronaker.app.ui.addProduct

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.GoogleMapRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Place
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductLocationViewModel (app: Application): BaseViewModel(app) {

    internal val TAG = AddProductLocationViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val newLocation: MutableLiveData<LatLng> = MutableLiveData()
    val placeName: MutableLiveData<String> = MutableLiveData()


    @Inject
    lateinit var context: Context

    @Inject
    lateinit var productRepository: ProductRepository
    @Inject
    lateinit var googleMapRepository: GoogleMapRepository


    private var getPlaceByidSubscription: Disposable? = null
    private var getPlaceWithLocationSubscription: Disposable? = null


    var mPlace: Place? = null

    fun getPlace():Place?{
        return mPlace
    }

    init {


    }

    override fun onCleared() {
        super.onCleared()
        getPlaceByidSubscription?.dispose()
        getPlaceWithLocationSubscription?.dispose()

    }

    fun selectPlace(location: Place) {


        location.placeId?.let { getPlaceByid(it) }


    }

    fun getPlaceByid(placeId: String) {
        getPlaceByidSubscription?.dispose()
        getPlaceByidSubscription = googleMapRepository.getPlaceDetails(placeId)
            .doOnSubscribe { }
            .doOnTerminate { }

            .subscribe(
                { result ->

                    result?.let {

                        mPlace=it
                        newLocation.value = it.latLng
                        placeName.value = it.mainText
                    }


                },
                {error-> error.message}
            )



    }

    fun updateLocation(target: LatLng) {
        getPlaceWithLocationSubscription?.dispose()

        getPlaceWithLocationSubscription = googleMapRepository.getGeocode(target)
            .doOnSubscribe {  }
            .doOnTerminate {  }
            .subscribe(
                { result ->


                    result?.let {
                        mPlace = it
                        mPlace?.latLng = target
                        placeName.value = it.mainText


                    }?: run{

                        mPlace = null
                        placeName.value = context.getString(R.string.title_search_your_location)
                    }


                },
                { error ->


                    mPlace = null
                    placeName.value = context.getString(R.string.title_search_your_location)
                    error.message


                }
            )


    }


}