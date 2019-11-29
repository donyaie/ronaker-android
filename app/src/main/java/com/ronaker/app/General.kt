package com.ronaker.app

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.onesignal.OneSignal
import com.ronaker.app.utils.FONT_PATH
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import com.ronaker.app.utils.LocaleHelper


class General : MultiDexApplication() {



    lateinit var  analytics: FirebaseAnalytics


    override fun onCreate() {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate()

        analytics = FirebaseAnalytics.getInstance(this)


        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()

//
//        if (BuildConfig.DEBUG)
//            com.facebook.stetho.Stetho.initializeWithDefaults(this)


        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(FONT_PATH)
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

    }



    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { LocaleHelper.onAttach(it, "en") })
        MultiDex.install(this)

    }


}