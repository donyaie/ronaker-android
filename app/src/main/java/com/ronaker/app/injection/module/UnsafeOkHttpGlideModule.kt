package com.ronaker.app.injection.module

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import java.io.InputStream
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.module.AppGlideModule
import com.ronaker.app.utils.SslUtils


@GlideModule
class UnsafeOkHttpGlideModule : AppGlideModule(){
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
    }


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = SslUtils.getUnsafeOkHttpClient()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(client.build())
        )
    }
}

