package com.ronaker.app.injection.module

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ronaker.app.BuildConfig
import okhttp3.OkHttpClient
import java.io.InputStream


@GlideModule
class CustomGlideModule : AppGlideModule() {


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
//        val client = SslUtils.getUnsafeOkHttpClient(context)
        val client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG)
            client.addNetworkInterceptor(StethoInterceptor())

        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(client.build())
        )
    }
}

