package com.ronaker.app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: com.ronaker.app.databinding.ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.viewModel = viewModel


    }





    companion object {
           fun newInstance(context: Context): Intent {
            return  Intent(context, LoginActivity::class.java)
        }
    }
}