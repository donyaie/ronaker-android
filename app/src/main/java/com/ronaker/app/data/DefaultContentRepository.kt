package com.ronaker.app.data

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.ronaker.app.base.Result
import com.ronaker.app.base.toResult
import com.ronaker.app.data.network.ContentApi
import com.ronaker.app.model.Media
import com.ronaker.app.model.toMediaModel
import com.ronaker.app.utils.FileUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import java.io.File
import java.io.IOException
import javax.inject.Inject


class DefaultContentRepository @Inject constructor(
    private val contentApi: ContentApi, private val context: Context,
    private val userRepository: UserRepository
) : ContentRepository {


    override fun uploadImageWithoutProgress(
        
        filePath: Uri
    ): Observable<Result<Media>> {
        return contentApi.uploadImageWithoutProgress(
            userRepository.getUserAuthorization(),userRepository.getUserLanguage(),
            createMultipartBody(filePath)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .map {
                it.toMediaModel()
            }
            .toResult()
    }

    override fun downloadFile(
        
        downloadLink: String,
        fileName: String
    ): Observable<Result<File?>> {
        return contentApi.download(userRepository.getUserAuthorization(), userRepository.getUserLanguage(),downloadLink)
            .flatMap { responseBodyResponse ->
                Observable.create(ObservableOnSubscribe<File?> { subscriber ->


                    try {
                        val file = FileUtils.getCacheContractFile(context, fileName)

                        if (file.exists())
                            file.delete()

                        val sink: BufferedSink = Okio.buffer(Okio.sink(file))

                        responseBodyResponse.body()?.source()?.let {
                            sink.writeAll(it)
                        }
                        sink.close()
                        subscriber.onNext(file)
                        subscriber.onComplete()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        subscriber.onError(e)
                    }


                })


            }.toResult()
    }


    override fun deleteImage( suid: String): Observable<Result<Boolean>> {
        return contentApi.deleteImage(userRepository.getUserAuthorization(),userRepository.getUserLanguage(), suid)
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
        return file.let { RequestBody.create(MediaType.parse("image/*"), it) }
    }


}