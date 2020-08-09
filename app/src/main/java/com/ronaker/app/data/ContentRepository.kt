package com.ronaker.app.data

import android.net.Uri
import com.ronaker.app.base.Result
import com.ronaker.app.model.Media
import io.reactivex.Observable
import java.io.File


interface ContentRepository {
    fun uploadImageWithoutProgress( filePath: Uri): Observable<Result<Media>>
    fun deleteImage( suid: String): Observable<Result<Boolean>>


    fun downloadFile(
        downloadLink: String,
        fileName: String
    ): Observable<Result<File?>>
}
