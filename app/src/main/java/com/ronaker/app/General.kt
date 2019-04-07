package com.ronaker.app

import android.app.Application
import android.content.Context
import android.content.Intent
import com.ronaker.app.injection.component.AppComponent
import com.ronaker.app.injection.component.DaggerAppComponent
import com.ronaker.app.injection.module.AppModule
import com.ronaker.app.ui.login.LoginActivity

class General : Application() {
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


    }

    private fun initDagger(app: General): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}