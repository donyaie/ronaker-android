package com.ronaker.app.data

import android.net.Uri
import androidx.core.net.toFile
import com.ronaker.app.base.CountingRequestBody
import com.ronaker.app.base.NetworkError
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ContentApi
import com.ronaker.app.data.network.response.ContentImageResponceModel
import com.ronaker.app.data.network.response.ProductCreateResponseModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response


class ContentRepository(val contentApi: ContentApi) {


    fun uploadImageWithoutProgress(token: String?, filePath: Uri?): Observable<Result<ContentImageResponceModel>> {
        return contentApi.uploadImageWithoutProgress("Token $token", createMultipartBody(filePath))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()
    }


    fun deleteImage(token: String?, suid: String): Observable<Result<ProductCreateResponseModel>> {
        return contentApi.deleteImage("Token $token", suid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).toResult()
    }



    private fun createMultipartBody(filePath: Uri?): MultipartBody.Part {
        val file = filePath?.toFile()
        val requestBody = createRequestBody(file)
        return MultipartBody.Part.createFormData("content", file?.name, requestBody)
    }

    private fun createRequestBody(file: File?): RequestBody {
        return RequestBody.create(MediaType.parse("image/*"), file!!)
    }

    fun uploadImage(token: String?, filePath: String): Flowable<Double> {
        return Flowable.create({ emitter ->
            try {
                contentApi.run { uploadImage("Token $token", createMultipartBody(filePath, emitter)).blockingGet() }

                emitter.onComplete()
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }, BackpressureStrategy.LATEST)
    }

    private fun createMultipartBody(filePath: String, emitter: FlowableEmitter<Double>): MultipartBody.Part {
        val file = File(filePath)
        return MultipartBody.Part.createFormData("content", file.name, createCountingRequestBody(file, emitter))
    }

    private fun createCountingRequestBody(file: File, emitter: FlowableEmitter<Double>): RequestBody {
        val requestBody = createRequestBody(file)
        return CountingRequestBody(requestBody, object : CountingRequestBody.Listener {
            override fun onRequestProgress(bytesWritten: Long, contentLength: Long) {
                val progress = 1.0 * bytesWritten / contentLength
                emitter.onNext(progress)
            }

        })
    }


    fun uploadImageAsync(token: String?, filePath: Uri, callback: FileUploadListener):Call<ContentImageResponceModel>? {
        val file = filePath.toFile()
        var call:Call<ContentImageResponceModel>?=null
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("content", file.name, mFile)
//        val filename = RequestBody.create(MediaType.parse("text/plain"), file.name)

        try {
            call = contentApi.uploadImageWithoutProgressr("Token $token", fileToUpload)
            call.enqueue(
                object : retrofit2.Callback<ContentImageResponceModel> {
                    override fun onFailure(call: Call<ContentImageResponceModel>, t: Throwable) {
                        callback.onFailure(NetworkError(t))
                    }

                    override fun onResponse(
                        call: Call<ContentImageResponceModel>,
                        response: Response<ContentImageResponceModel>
                    ) {

                        if (response.isSuccessful)
                            callback.onSuccess(response.body())
                    }

                })
        } catch (e: java.lang.Exception) {
            callback.onFailure(NetworkError(e))
        }
        return call

    }


    interface FileUploadListener {
        fun onFailure(networkError: NetworkError)

        fun onSuccess(body: ContentImageResponceModel?)
    }
}