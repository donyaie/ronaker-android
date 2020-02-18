package com.ronaker.app.ui.profileEmailEdit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ronaker.app.utils.Alert
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.finishSafe


class ProfileEmailEditActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileEmailEditBinding
    private lateinit var viewModel: ProfileEmailEditViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileEmailEditActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_email_edit)

        viewModel = ViewModelProvider(this).get(ProfileEmailEditViewModel::class.java)

        binding.viewModel = viewModel


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.visibility=View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })

        viewModel.goNext.observe(this, Observer { value ->
            if (value == true) {
                finishSafe()
            }
        })


        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }


        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.saveButton.setOnClickListener {
            if(binding.emailInput.checkValid() ){
                viewModel.saveInfo(binding.emailInput.text)
            }

        }





    }

    override fun onStart() {

        super.onStart()
        viewModel.loadData()


    }




}