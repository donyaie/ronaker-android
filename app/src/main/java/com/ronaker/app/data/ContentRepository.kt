package com.ronaker.app.data

import android.net.Uri
import com.ronaker.app.base.NetworkError
import com.ronaker.app.base.Result
import com.ronaker.app.data.network.response.ContentImageResponceModel
import com.ronaker.app.data.network.response.FreeResponseModel
import io.reactivex.*
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.Call


interface ContentRepository {
    fun uploadImageWithoutProgress(token: String?, filePath: Uri): Observable<Result<ContentImageResponceModel>>
    fun deleteImage(token: String?, suid: String): Observable<Result<FreeResponseModel>>

    fun uploadImage(token: String?, filePath: String): Flowable<Double>
    fun uploadImageAsync(token: String?, filePath: Uri, callback: ContentRepository.FileUploadListener): Call<ContentImageResponceModel>?
    interface FileUploadListener {
        fun onFailure(networkError: NetworkError)

        fun onSuccess(body: ContentImageResponceModel?)
    }
}
