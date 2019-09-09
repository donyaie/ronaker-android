package com.ronaker.app.ui.addProduct

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.GoogleMapRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Place
import com.ronaker.app.model.converGeoToPlace
import com.ronaker.app.model.toPlace
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductLocationViewModel : BaseViewModel() {

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
                    if (result.result != null) {

                        mPlace = result.result!!.toPlace()
                        newLocation.value = LatLng(mPlace!!.lat!!, mPlace!!.lng!!)
                        placeName.value = mPlace!!.mainText
                    } else {

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

                    if (result.results != null) {

                        mPlace = result.converGeoToPlace()

                        if (mPlace != null) {
                            mPlace?.lat = target.latitude
                            mPlace?.lng = target.longitude

                            placeName.value = mPlace!!.mainText
                        } else {
                            mPlace = null
                            placeName.value = context.getString(R.string.title_search_your_location)
                        }
                    } else {


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