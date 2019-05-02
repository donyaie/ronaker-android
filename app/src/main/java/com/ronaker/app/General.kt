package com.ronaker.app

import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.ronaker.app.injection.component.AppComponent
import com.ronaker.app.injection.component.DaggerAppComponent
import com.ronaker.app.injection.module.AppModule
import com.ronaker.app.ui.login.LoginActivity
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


class General : MultiDexApplication() {
    lateinit var General: AppComponent

    companion object {
        lateinit var context: Context

        fun newInstance(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this;
        General=initDagger(this)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/regular.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initDagger(app: General): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}