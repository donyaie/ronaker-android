package com.ronaker.app.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.login.LoginActivity
import com.ronaker.app.utils.AnimationHelper
import java.util.*
import kotlin.concurrent.schedule


class SplashActivity : BaseActivity() {

    private lateinit var binding: com.ronaker.app.databinding.ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)


        viewModel.goLogin.observe(this, Observer { value ->
            if (value == true) {


                Timer("startLogin", false).schedule(1000) {
                    startActivity(LoginActivity.newInstance(this@SplashActivity))

                    finish()

                }
            }
        })

        viewModel.goDashboard.observe(this, Observer { value ->
            if (value == true) {

                Timer("startDashboard", false).schedule(1000) {
                    startActivity(DashboardActivity.newInstance(this@SplashActivity))
                    finish()
                }
            }
        })

        binding.viewModel = viewModel


    }

    companion object {
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            return intent
        }
    }


}
