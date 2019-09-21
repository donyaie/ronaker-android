package com.ronaker.app.ui.profileIdentify

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
import com.ronaker.app.ui.profilePayment.ProfilePaymentActivity
import kotlinx.android.synthetic.main.activity_login.view.*


class ProfileIdentifyActivity : BaseActivity() {

    private val TAG = ProfileIdentifyActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileIndentifyBinding
    private lateinit var viewModel: ProfileIdentifyViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, ProfileIdentifyActivity::class.java)
            var boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_indentify)

        viewModel = ViewModelProviders.of(this).get(ProfileIdentifyViewModel::class.java)

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






        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.loading.hideLoading()


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