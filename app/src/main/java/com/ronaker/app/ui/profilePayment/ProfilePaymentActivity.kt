package com.ronaker.app.ui.profilePayment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity


class ProfilePaymentActivity : BaseActivity() {

    private val TAG = ProfilePaymentActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfilePaymentBinding
    private lateinit var viewModel: ProfilePaymentViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, ProfilePaymentActivity::class.java)
            var boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_payment)

        viewModel = ViewModelProviders.of(this).get(ProfilePaymentViewModel::class.java)

        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.retry.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showRetry()
            } else
                binding.loading.hideRetry()
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }






        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


    }

    override fun onStart() {

        super.onStart()

        if (isFistStart()) {

           viewModel.loadData()


        }


    }



    override fun onBackPressed() {
        super.onBackPressed();
    }


}