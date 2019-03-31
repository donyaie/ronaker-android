package com.ronaker.app.ui.splash

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Post
import com.ronaker.app.network.PostApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class SplashViewModel: BaseViewModel(){

    private lateinit var subscription: Disposable

    init{

    }



    override fun onCleared() {
        super.onCleared()
//        subscription.dispose()
    }

}