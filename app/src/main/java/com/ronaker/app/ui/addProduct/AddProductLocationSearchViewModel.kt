package com.ronaker.app.ui.addProduct

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.GoogleMapRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Place
import com.ronaker.app.model.toPlaceList
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class AddProductLocationSearchViewModel : BaseViewModel() {

    internal val TAG = AddProductLocationSearchViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    @Inject
    lateinit var context: Context

    @Inject
    lateinit var googleMapRepository: GoogleMapRepository


    private var searchSubscription: Disposable? = null
    private var updateproductSubscription: Disposable? = null

    val selectedPlace: MutableLiveData<Place> = MutableLiveData()


    var dataList: ArrayList<Place> = ArrayList()
    var listAdapter: LocationSearchAdapter


    init {

        listAdapter = LocationSearchAdapter(dataList, this)

    }

    override fun onCleared() {
        super.onCleared()
        searchSubscription?.dispose()
        updateproductSubscription?.dispose()

    }

    fun searchLocation(filter: String) {

        searchSubscription?.dispose()

        searchSubscription = googleMapRepository.getQueryAutocomplete(
            filter, null
        )
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe(
                { result ->
                    if (result.predictions != null) {

                        dataList.clear()
                        result.predictions?.let {  dataList.addAll(it.toPlaceList())  }
                        listAdapter.notifyDataSetChanged()

                    } else {

                    }
                },
                { error -> error.message }
            )



    }

    fun selectPlace(data: Place) {
        selectedPlace.value = data

    }

}