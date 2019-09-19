package com.ronaker.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.ronaker.app.injection.component.AppComponent
import com.ronaker.app.injection.component.DaggerAppComponent
import com.ronaker.app.injection.module.AppModule
import com.ronaker.app.ui.login.LoginActivity
import com.ronaker.app.utils.FONT_PATH
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import com.ronaker.app.utils.LocaleHelper




class General : MultiDexApplication() {
    lateinit var General: AppComponent

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun newInstance(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this;
        General=initDagger()

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

        base?.let { context= base}
    }



    private fun initDagger(): AppComponent =
        DaggerAppComponent.builder()
//            .appModule(AppModule(app))app

            .build()
}