package com.ronaker.app.data

import android.net.Uri
import com.ronaker.app.base.Result
import com.ronaker.app.model.Media
import io.reactivex.Observable


interface ContentRepository {
    fun uploadImageWithoutProgress(token: String?, filePath: Uri): Observable<Result<Media>>
    fun deleteImage(token: String?, suid: String): Observable<Result<Boolean>>


}
