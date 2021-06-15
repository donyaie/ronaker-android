package com.ronaker.app.ui.docusign


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.system.measureTimeMillis
@HiltViewModel
class DocusignViewModel @Inject constructor(
    private val userRepository: UserRepository,


) : BaseViewModel() {

    private val TAG = DocusignViewModel::class.java.simpleName




    init {
        getUrl()
    }

    val error: MutableLiveData<String> = MutableLiveData()




    val loadUrl: MutableLiveData<String> = MutableLiveData()

    private var subscription: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


    fun getUrl(){
        subscription?.dispose()

        subscription = userRepository
            .docusignAuth()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

            .doOnSubscribe {


            }
            .doOnTerminate {

            }

            .subscribe { result ->

                if(result.isSuccess()){
                    loadUrl.postValue(result.data)
                }
                else{

                    error.postValue(result.error?.message)
                }

            }

    }





}