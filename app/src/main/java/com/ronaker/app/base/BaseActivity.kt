package com.ronaker.app.base

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.ronaker.app.utils.LocaleHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper



abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState)



    }

    var startCount=0


    override fun onStart() {
        super.onStart()
        startCount += 1

    }


    fun isFistStart():Boolean{
        return startCount<=1
    }


    protected override fun attachBaseContext(newBase: Context) {



        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)))
    }


    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }




    fun startActivityMakeScene(intent: Intent?) {
        super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

    }

     fun finishSafe() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAfterTransition();
        }

        else
         super.finish();
    }


}