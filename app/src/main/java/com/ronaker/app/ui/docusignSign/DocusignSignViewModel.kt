package com.ronaker.app.ui.docusignSign


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DocusignSignViewModel @Inject constructor(
    private val orderRepository: OrderRepository,


) : BaseViewModel() {

    private val TAG = DocusignSignViewModel::class.java.simpleName



    val error: MutableLiveData<String> = MutableLiveData()




    val loadUrl: MutableLiveData<String> = MutableLiveData()

    private var subscription: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


    fun getUrl(suid:String){
        subscription?.dispose()

        subscription = orderRepository
            .signDocusign(suid)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

            .doOnSubscribe {


            }
            .doOnTerminate {

            }

            .subscribe { result ->

                if(result.isSuccess()){
                    loadUrl.postValue(result.data)
                }else{

                    error.postValue(result.error?.message)
                }

            }

    }





}