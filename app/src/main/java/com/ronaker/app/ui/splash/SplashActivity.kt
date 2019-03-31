package com.ronaker.app.ui.splash

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.login.LoginActivity
import java.util.*
import kotlin.concurrent.schedule

class  SplashActivity : BaseActivity(){

    private lateinit var binding: com.ronaker.app.databinding.ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this, R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        binding.viewModel = viewModel
        nextActivity()

    }



    private fun nextActivity() {
        Timer("login",false).schedule(1000){
            startActivity(LoginActivity.newInstance(this@SplashActivity))
            finish()
        }
    }


    override fun finish() {
        super.finish()
    }

}