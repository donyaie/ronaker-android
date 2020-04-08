package com.ronaker.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*


object SslUtils {

    //download pem
    //openssl s_client -showcerts -connect myserver.com:443 </dev/null 2>/dev/null|openssl x509 -outform PEM > app/main/assets/my_service_certifcate.pem
    private val TAG = SslUtils::class.java.name

    fun getUnsafeOkHttpClientAll(): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> {

                    return arrayOf()
                }


                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }

            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }


    // Create a trust manager that does not validate certificate chains
    // Install the all-trusting trust manager
    // Create an ssl socket factory with our all-trusting manager
    fun getUnsafeOkHttpClient(context: Context): OkHttpClient.Builder {
        try {

            val builder = OkHttpClient.Builder()

            getTrustAllHostsSSLSocketFactory()?.let {
                builder.sslSocketFactory(it.first, it.second)
            }

            getSslContextForCertificateFile(
                context,
                "my_certificate.pem"
            )?.let {

                builder.sslSocketFactory(it.first.socketFactory, it.second)

            }





            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun getSslContextForCertificateFile(
        context: Context,
        fileName: String
    ): Pair<SSLContext, X509TrustManager>? {
        try {
            val keyStore = getKeyStore(context, fileName)
            val sslContext = SSLContext.getInstance("SSL")
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())


            val manager = trustManagerFactory.trustManagers[0] as X509TrustManager
            return Pair(sslContext, manager)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)

            return null
        }
    }

    private fun getKeyStore(context: Context, fileName: String): KeyStore? {
        var keyStore: KeyStore? = null
        try {
            val assetManager = context.assets
            val cf = CertificateFactory.getInstance("X.509")
            val caInput = assetManager.open(fileName)
            val ca: Certificate
            try {
                ca = cf.generateCertificate(caInput)
                AppDebug.log("SslUtilsAndroid", "ca=" + (ca as X509Certificate).subjectDN)
            } finally {
                caInput.close()
            }

            val keyStoreType = KeyStore.getDefaultType()
            keyStore = KeyStore.getInstance(keyStoreType)
            keyStore!!.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return keyStore
    }

    private fun getTrustAllHostsSSLSocketFactory(): Pair<SSLSocketFactory, X509TrustManager>? {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager

            return Pair(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        } catch (e: KeyManagementException) {
            e.printStackTrace()
            return null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

    }
}