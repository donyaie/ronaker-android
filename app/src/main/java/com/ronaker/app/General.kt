package com.ronaker.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.onesignal.OneSignal
import com.ronaker.app.utils.AppNotificationOpenedHandler
import com.ronaker.app.utils.FONT_PATH
import com.ronaker.app.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


@HiltAndroidApp
class General : Application() {

    private val ONESIGNAL_APP_ID = "cd7faabe-078b-44f5-a752-39bcbb7837f7"

    lateinit var analytics: FirebaseAnalytics


    override fun onCreate() {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate()



        analytics = FirebaseAnalytics.getInstance(this)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        Branch.getAutoInstance(this)



        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.setNotificationOpenedHandler(AppNotificationOpenedHandler(this))
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)


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


    override fun attachBaseContext(base: Context) {
        MultiDex.install(this)

        super.attachBaseContext(LocaleHelper.onAttach(base))


    }


}