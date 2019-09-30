package com.ronaker.app.utils

import com.ronaker.app.General
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.*


class UnsafeOkHttpClient {



    companion object {
        // Create a trust manager that does not validate certificate chains
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        fun getUnsafeOkHttpClient1(): OkHttpClient {
            try {

                val builder = OkHttpClient.Builder()


                SslUtils.getTrustAllHostsSSLSocketFactory()?.let {
                    builder.sslSocketFactory(it)
                }

                builder.sslSocketFactory(
                    SslUtils.getSslContextForCertificateFile(
                        General.context,
                        "my_certificate.pem"
                    ).socketFactory
                )





                return builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }


        fun getUnsafeOkHttpClient(): OkHttpClient {
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                    override fun getAcceptedIssuers(): Array<X509Certificate> {

                        return arrayOf()
                    }


                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                    }

                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                    }

                })

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.getSocketFactory()

                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { hostname, session -> true }

                return builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }





    }






}