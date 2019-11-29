package com.ronaker.app.base

import com.google.gson.JsonParser
import com.ronaker.app.utils.AppDebug
import retrofit2.HttpException
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class NetworkError(error: Throwable) {

    private val TAG = NetworkError::class.java.name

    enum class HttpError {
        HttpResponseSuccessOK,
        HttpResponseSuccess,
        HttpResponseRedirection,
        HttpResponseRedirectionMovedPermanently,
        HttpResponseRedirectionNotModified,
        HttpResponseUnauthenticated,
        HttpResponseClientErrorBadRequest,
        HttpResponseClientErrorUnauthorized,
        HttpResponseClientErrorForbidden,
        HttpResponseClientErrorNotFound,
        HttpResponseClientErrorMethodNotAllowed,
        HttpResponseClientError,
        HttpResponseServerError,
        HttpResponseNetworkError,
        HttpResponseUnexpectedError,
        HttpResponseNGINXError,
        HttpResponseFailure,
        HttpResponseConnectionFailure,
        HttpResponseNetworkTimeout,
        HttpResponseCancel,
        HttpResponseClientErrorTooMuchTry
    }

    var message = "An error occurred"
    var responseCode: Int? = null
    var code: String? = null
    var httpError: HttpError? = null

    var exceptionError: Throwable? = null

    companion object {

        var error_unverified_phone_number = "unverified_phone_number"
    }

    init {

        exceptionError = error
        when (error) {
            is HttpException -> {
                val errorJsonString = error.response()
                    ?.errorBody()?.string()

                try {

                    val json = JsonParser().parse(errorJsonString).asJsonObject

                    try {
                        if (json.has("detail"))
                            this.message = json["detail"]
                                .asString
                    } catch (e: Exception) {
                        AppDebug.log(TAG, e)
                    }
                    try {
                        if (json.has("detail_code"))
                            this.code = json["detail_code"]
                                .asString
                    } catch (e: Exception) {
                        AppDebug.log(TAG, e)
                    }
                } catch (ex: Exception) {
                    AppDebug.log(TAG, ex)
                }

                this.responseCode = error.code()

                responseCode?.let { httpError = getHttpError(it) }

            }
            is EOFException -> {

                message = "Empty Exception"
                responseCode = 200
                code = "Empty"
                httpError = HttpError.HttpResponseSuccessOK


            }
            is SocketTimeoutException -> {

                message = "Network Timeout"
                responseCode = null
                code = "HttpResponseNetworkTimeout"
                httpError = HttpError.HttpResponseNetworkTimeout

            }
            is ConnectException -> {

                message = "Connection Failure"
                responseCode = null
                code = "HttpResponseConnectionFailure"
                httpError = HttpError.HttpResponseConnectionFailure

            }
            is IOException -> {

                message = "Network Error"
                responseCode = null
                code = "HttpResponseNetworkError"
                httpError = HttpError.HttpResponseNetworkError

            }
            else -> {

                message = "Failure"
                responseCode = null
                code = "HttpResponseFailure"
                httpError = HttpError.HttpResponseFailure
            }
        }


    }


    private fun getHttpError(code: Int): HttpError {


        return when (code) {
            200 -> {
                HttpError.HttpResponseSuccessOK
            }
            301 -> {
                HttpError.HttpResponseRedirectionMovedPermanently
            }
            304 -> {
                HttpError.HttpResponseRedirectionNotModified
            }
            400 -> {
                HttpError.HttpResponseClientErrorBadRequest
            }
            401 -> {
                HttpError.HttpResponseClientErrorUnauthorized
            }
            403 -> {
                HttpError.HttpResponseClientErrorForbidden
            }
            404 -> {
                HttpError.HttpResponseClientErrorNotFound
            }
            405 -> {
                HttpError.HttpResponseClientErrorMethodNotAllowed
            }
            429 -> {
                HttpError.HttpResponseClientErrorTooMuchTry
            }
            502 -> {
                HttpError.HttpResponseNGINXError
            }
            in 200..299 -> {
                HttpError.HttpResponseSuccess
            }
            in 300..399 -> {
                HttpError.HttpResponseRedirection
            }
            in 400..499 -> {
                HttpError.HttpResponseClientError
            }
            in 500..599 -> {
                HttpError.HttpResponseServerError
            }
            else -> {
                HttpError.HttpResponseUnexpectedError
            }
        }
    }


}