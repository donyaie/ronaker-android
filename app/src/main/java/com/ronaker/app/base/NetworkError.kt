package com.ronaker.app.base

import com.google.gson.JsonParser
import com.ronaker.app.utils.Debug
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException


class NetworkError(error: Throwable) {

    internal val TAG = NetworkError::class.java.name

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

    var detail = "An error occurred"
    var responseCode: Int? = null;
    var detail_code: String? = null;
    var http_error: HttpError? = null;

    var exception_error: Throwable? = null

    companion object {

        var error_unverified_phone_number = "unverified_phone_number"
    }

    init {

        exception_error = error
        if (error is HttpException) {
            val errorJsonString = (error as HttpException).response()
                .errorBody()?.string()
            try {
                this.detail = JsonParser().parse(errorJsonString)
                    .asJsonObject["detail"]
                    .asString
            } catch (e: Exception) {
                Debug.Log(TAG, e)
            }
            try {
                this.detail_code = JsonParser().parse(errorJsonString)
                    .asJsonObject["detail_code"]
                    .asString
            } catch (e: Exception) {
                Debug.Log(TAG, e)
            }
            this.responseCode = error.code();

            http_error = getHttpError(responseCode!!)

        } else {

            if (error is SocketTimeoutException) {

                detail = "Network Timeout"
                responseCode = null
                detail_code = "HttpResponseNetworkTimeout"
                http_error = HttpError.HttpResponseNetworkTimeout

            } else if (error is ConnectException) {

                detail = "Connection Failure"
                responseCode = null
                detail_code = "HttpResponseConnectionFailure"
                http_error = HttpError.HttpResponseConnectionFailure

            } else if (error is IOException) {

                detail = "Network Error"
                responseCode = null
                detail_code = "HttpResponseNetworkError"
                http_error = HttpError.HttpResponseNetworkError

            } else {

                detail = "Failure"
                responseCode = null
                detail_code = "HttpResponseFailure"
                http_error = HttpError.HttpResponseFailure
            }

        }
    }


    private fun getHttpError(code: Int): HttpError {


        return if (code == 200) {
            HttpError.HttpResponseSuccessOK
        } else if (code == 301) {
            HttpError.HttpResponseRedirectionMovedPermanently
        } else if (code == 304) {
            HttpError.HttpResponseRedirectionNotModified
        } else if (code == 400) {
            HttpError.HttpResponseClientErrorBadRequest
        } else if (code == 401) {
            HttpError.HttpResponseClientErrorUnauthorized
        } else if (code == 403) {
            HttpError.HttpResponseClientErrorForbidden
        } else if (code == 404) {
            HttpError.HttpResponseClientErrorNotFound
        } else if (code == 405) {
            HttpError.HttpResponseClientErrorMethodNotAllowed
        } else if (code == 429) {
            HttpError.HttpResponseClientErrorTooMuchTry
        } else if (code == 502) {
            HttpError.HttpResponseNGINXError
        } else if (code >= 200 && code < 300) {
            HttpError.HttpResponseSuccess
        } else if (code >= 300 && code < 400) {
            HttpError.HttpResponseRedirection
        } else if (code >= 400 && code < 500) {
            HttpError.HttpResponseClientError
        } else if (code >= 500 && code < 600) {
            HttpError.HttpResponseServerError
        } else {
            HttpError.HttpResponseUnexpectedError
        }
    }


}