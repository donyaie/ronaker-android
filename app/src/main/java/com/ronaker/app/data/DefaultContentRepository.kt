package com.ronaker.app.data

import android.net.Uri
import androidx.core.net.toFile
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ContentApi
import com.ronaker.app.model.Media
import com.ronaker.app.model.toMediaModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class DefaultContentRepository(private val contentApi: ContentApi) : ContentRepository {


    override fun uploadImageWithoutProgress(
        token: String?,
        filePath: Uri
    ): Observable<Result<Media>> {
        return contentApi.uploadImageWithoutProgress("Token $token", createMultipartBody(filePath))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {
                it.toMediaModel()
            }
            .toResult()
    }


    override fun deleteImage(token: String?, suid: String): Observable<Result<Boolean>> {
        return contentApi.deleteImage("Token $token", suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                true
            }
            .toResult()
    }


   private fun createMultipartBody(filePath: Uri): MultipartBody.Part {
        val file = filePath.toFile()
        val requestBody = createRequestBody(file)
        return requestBody.let { MultipartBody.Part.createFormData("content", file.name, it) }
    }

    private fun createRequestBody(file: File): RequestBody {
        return file .let {  RequestBody.create(MediaType.parse("image/*"), it)}
    }

//    override fun uploadImage(token: String?, filePath: String): Flowable<Double> {
//        return Flowable.create({ emitter ->
//            try {
//                contentApi.run {
//                    uploadImage(
//                        "Token $token",
//                        createMultipartBody(filePath, emitter)
//                    ).blockingGet()
//                }
//
//                emitter.onComplete()
//            } catch (e: Exception) {
//                emitter.tryOnError(e)
//            }
//        }, BackpressureStrategy.LATEST)
//    }

//    fun createMultipartBody(
//        filePath: String,
//        emitter: FlowableEmitter<Double>
//    ): MultipartBody.Part {
//        val file = File(filePath)
//        return MultipartBody.Part.createFormData(
//            "content",
//            file.name,
//            createCountingRequestBody(file, emitter)
//        )
//    }

//    fun createCountingRequestBody(file: File, emitter: FlowableEmitter<Double>): RequestBody {
//        val requestBody = createRequestBody(file)
//        return requestBody.let {
//            CountingRequestBody(it, object : CountingRequestBody.Listener {
//                override fun onRequestProgress(bytesWritten: Long, contentLength: Long) {
//                    val progress = 1.0 * bytesWritten / contentLength
//                    emitter.onNext(progress)
//                }
//
//            })
//        }
//    }


//    override fun uploadImageAsync(
//        token: String?,
//        filePath: Uri,
//        callback: ContentRepository.FileUploadListener
//    ): Call<ContentImageResponceModel>? {
//        val file = filePath.toFile()
//        var call: Call<ContentImageResponceModel>? = null
//        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
//        val fileToUpload = MultipartBody.Part.createFormData("content", file.name, mFile)
////        val filename = RequestBody.create(MediaType.parse("text/plain"), file.name)
//
//        try {
//            call = contentApi.uploadImageWithoutProgressr("Token $token", fileToUpload)
//            call.enqueue(
//                object : retrofit2.Callback<ContentImageResponceModel> {
//                    override fun onFailure(call: Call<ContentImageResponceModel>, t: Throwable) {
//                        callback.onFailure(NetworkError(t))
//                    }
//
//                    override fun onResponse(
//                        call: Call<ContentImageResponceModel>,
//                        response: Response<ContentImageResponceModel>
//                    ) {
//
//                        if (response.isSuccessful)
//                            callback.onSuccess(response.body())
//                    }
//
//                })
//        } catch (e: java.lang.Exception) {
//            callback.onFailure(NetworkError(e))
//        }
//        return call
//
//    }


}