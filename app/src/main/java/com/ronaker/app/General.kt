package com.ronaker.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.docusign.androidsdk.DSEnvironment
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.util.DSMode
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.onesignal.OneSignal
import com.ronaker.app.utils.AppNotificationOpenedHandler
import com.ronaker.app.utils.FONT_PATH
import com.ronaker.app.utils.LocaleHelper
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


@HiltAndroidApp
class General : Application() {

    private val ONESIGNAL_APP_ID = "cd7faabe-078b-44f5-a752-39bcbb7837f7"
    private val STRIPE_PUBLISH_KEY = "pk_test_51HMLSyDlgne5zIM62PlHSeg8VGS3g9gcZk4RFqhKaALvFn4dv6bnpAT2a9yElV64C7J0jK1HvqrRlycMNaLbtLvj002kqmJGTZ"

    lateinit var analytics: FirebaseAnalytics


    override fun onCreate() {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate()

        PaymentConfiguration.init(
            applicationContext,
            STRIPE_PUBLISH_KEY
        )



        DocuSign.init(


            this, // the Application Context
            "f8cd5587-58e6-408d-abc6-b4ad0f088207", // Same as Client Id
            "5b6e2992-41f0-4fb5-afb2-cec52ec3c0f2",
            "ronaker://docusign",
            DSMode.DEBUG
        );
        DocuSign.getInstance().setEnvironment(DSEnvironment.DEMO_ENVIRONMENT);



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