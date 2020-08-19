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


    lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate()

        analytics = FirebaseAnalytics.getInstance(this)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        Branch.getAutoInstance(this)


        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .setNotificationOpenedHandler(AppNotificationOpenedHandler(this))
            .init()




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