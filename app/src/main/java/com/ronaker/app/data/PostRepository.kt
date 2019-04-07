package com.ronaker.app.data

import com.ronaker.app.data.network.PostApi
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.model.Post
import com.ronaker.app.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PostRepository (val postApi: PostApi){
    fun getPosts(): Observable<Result<List<Post>>> {
       return postApi.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()

    }
}