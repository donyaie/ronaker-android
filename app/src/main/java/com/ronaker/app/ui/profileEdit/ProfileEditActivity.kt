package com.ronaker.app.ui.profileEdit

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
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberActivity
import com.ronaker.app.ui.profileEmailEdit.ProfileEmailEditActivity
import com.ronaker.app.ui.profileImage.ProfileImageActivity
import com.ronaker.app.ui.profileNameEdit.ProfileNameEditActivity
import com.ronaker.app.ui.profilePaymentList.ProfilePaymentListActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.startActivityMakeScene


class ProfileEditActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileEditBinding
    private lateinit var viewModel: ProfileEditViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileEditActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)

        viewModel = ViewModelProvider(this).get(ProfileEditViewModel::class.java)

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


        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }


        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


        binding.avatarEdit.setOnClickListener {

            startActivityMakeScene(ProfileImageActivity.newInstance(this, viewModel.getAvatar()))
        }

        binding.paymentLayout.setOnClickListener {

            startActivityMakeScene(ProfilePaymentListActivity.newInstance(this))

        }
        binding.numberLayout.setOnClickListener {

            startActivityMakeScene(PhoneNumberActivity.newInstance(this))

        }

        binding.nameLayout.setOnClickListener {

            startActivityMakeScene(ProfileNameEditActivity.newInstance(this))

        }

        binding.mailLayout.setOnClickListener {

            startActivityMakeScene(ProfileEmailEditActivity.newInstance(this))

        }


    }

    override fun onStart() {

        super.onStart()
        viewModel.loadData()


    }




}