package com.ronaker.app.ui.profileEdit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.docusign.DocusignActivity
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberActivity
import com.ronaker.app.ui.profileAuthorization.ProfileAuthorizationActivity
import com.ronaker.app.ui.profileImage.ProfileImageActivity
import com.ronaker.app.ui.profileNameEdit.ProfileNameEditActivity
import com.ronaker.app.ui.profilePaymentList.ProfilePaymentListActivity
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileEditBinding
    private  val viewModel: ProfileEditViewModel by viewModels()


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileEditActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)

        binding.viewModel = viewModel

        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, { value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })


        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }


        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


        binding.avatarEdit.setOnClickListener {

            startActivity(ProfileImageActivity.newInstance(this, viewModel.getAvatar()))
        }

        binding.paymentLayout.setOnClickListener {

            startActivity(ProfilePaymentListActivity.newInstance(this))

        }
        binding.numberLayout.setOnClickListener {

            startActivity(PhoneNumberActivity.newInstance(this))

        }

        binding.nameLayout.setOnClickListener {

            startActivity(ProfileNameEditActivity.newInstance(this))

        }

//        binding.mailLayout.setOnClickListener {
//
//            startActivity(ProfileEmailEditActivity.newInstance(this))
//
//        }


        viewModel.smartIDComplete.observe(this, { value ->
            if (value == true) {

                binding.authLayout.isClickable = false
                binding.authImage.setImageResource(R.drawable.ic_complete)
            } else {

                binding.authLayout.isClickable = true
                binding.authImage.setImageResource(R.drawable.ic_guid_warning)
            }
        })

        binding.authLayout.setOnClickListener {


            startActivity(ProfileAuthorizationActivity.newInstance(this))
        }

        binding.docusignLayout.setOnClickListener {


            startActivity(DocusignActivity.newInstance(this))
        }




    }

    override fun onStart() {

        super.onStart()
        viewModel.loadData()


    }


}