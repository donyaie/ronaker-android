package com.ronaker.app.ui.profileEmailVerify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.Alert


class ProfileEmailVerifyActivity : BaseActivity() {

//    private val TAG = ProfileSettingActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileEmailVerifyBinding
    private lateinit var viewModel: ProfileEmailVerifyViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileEmailVerifyActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_email_verify)

        viewModel = ViewModelProvider(this).get(ProfileEmailVerifyViewModel::class.java)

        binding.viewModel = viewModel



        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) Alert.makeTextError(this, errorMessage)
        })
        viewModel.goNex.observe(this, Observer {
            finish()
        })


    }

    override fun onBackPressed() {

    }

    override fun onStart() {

        super.onStart()
//
//        if (isFistStart()) {

        viewModel.loadData()


//        }


    }


}