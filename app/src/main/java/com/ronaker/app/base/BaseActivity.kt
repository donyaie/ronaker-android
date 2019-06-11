package com.ronaker.app.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.ronaker.app.utils.LocaleHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper



abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


    protected override fun attachBaseContext(newBase: Context) {



        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)))
    }

}