package com.ronaker.app.ui.searchLocationDialog

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.GoogleMapRepository
import com.ronaker.app.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject


@HiltViewModel
class AddProductLocationSearchViewModel @Inject constructor(
    private val googleMapRepository: GoogleMapRepository
) : BaseViewModel() {

    internal val TAG = AddProductLocationSearchViewModel::class.java.name




    private var searchSubscription: Disposable? = null
    private var updateproductSubscription: Disposable? = null

    val selectedPlace: MutableLiveData<Place> = MutableLiveData()


    var dataList: ArrayList<Place> = ArrayList()
    var listAdapter: LocationSearchAdapter


    init {

        listAdapter =
            LocationSearchAdapter(
                dataList,
                this
            )

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
                    result?.let {
                        dataList.clear()
                        dataList.addAll(it)
                        listAdapter.notifyDataSetChanged()
                    }

                },
                { error -> error.message }
            )


    }

    fun selectPlace(data: Place) {
        selectedPlace.value = data

    }

}