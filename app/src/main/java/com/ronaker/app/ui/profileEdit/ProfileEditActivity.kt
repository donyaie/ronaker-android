package com.ronaker.app.ui.profileEdit

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
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberActivity


class ProfileEditActivity : BaseActivity() {

    private val TAG = ProfileEditActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileEditBinding
    private lateinit var viewModel: ProfileEditViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, ProfileEditActivity::class.java)
            var boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_explore)

        viewModel = ViewModelProviders.of(this).get(ProfileEditViewModel::class.java)

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



        viewModel.imageComplete.observe(this, Observer { value ->
            if (value == true) {
                binding.imageImage.setImageResource(R.drawable.ic_complete)
            } else
                binding.imageImage.setImageResource(R.drawable.ic_chevron_right)
        })

        viewModel.signComplete.observe(this, Observer { value ->
            if (value == true) {
                binding.signImage.setImageResource(R.drawable.ic_complete)
            } else
                binding.signImage.setImageResource(R.drawable.ic_chevron_right)
        })


        viewModel.phoneComplete.observe(this, Observer { value ->
            if (value == true) {
                binding.phoneImage.setImageResource(R.drawable.ic_complete)
            } else
                binding.phoneImage.setImageResource(R.drawable.ic_chevron_right)
        })


        viewModel.identityComplete.observe(this, Observer { value ->
            if (value == true) {
                binding.identityImage.setImageResource(R.drawable.ic_complete)
            } else
                binding.identityImage.setImageResource(R.drawable.ic_chevron_right)
        })

        viewModel.peymentComplete.observe(this, Observer { value ->
            if (value == true) {
                binding.paymentImage.setImageResource(R.drawable.ic_complete)
            } else
                binding.paymentImage.setImageResource(R.drawable.ic_chevron_right)
        })



        binding.identityLayout.setOnClickListener {


        }


        binding.signLayout.setOnClickListener {


        }

        binding.imageLayout.setOnClickListener {


        }

        binding.phoneLayout.setOnClickListener {


            startActivity(PhoneNumberActivity.newInstance(this))

        }

        binding.paymentLayout.setOnClickListener {


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