package com.ronaker.app.data

import com.ronaker.app.data.network.PostApi
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ContentApi
import com.ronaker.app.model.Post
import com.ronaker.app.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class ContentRepository (val contentApi: ContentApi){
//    fun uploadImage(token:String,file:File): Observable<Result<List<Post>>> {
//       return contentApi.uploadImage(token)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//
//    }
}