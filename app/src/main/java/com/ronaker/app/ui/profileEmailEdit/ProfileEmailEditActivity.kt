package com.ronaker.app.ui.profileEmailEdit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileEmailEditActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileEmailEditBinding
    private  val viewModel: ProfileEmailEditViewModel by viewModels()


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileEmailEditActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_email_edit)

        binding.viewModel = viewModel


        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, {value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })

        viewModel.goNext.observe(this, {value ->
            if (value == true) {
                finish()
            }
        })


        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }


        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.saveButton.setOnClickListener {
            if (binding.emailInput.checkValid()) {
                viewModel.saveInfo(binding.emailInput.text)
            }

        }


    }

    override fun onStart() {

        super.onStart()
        viewModel.loadData()


    }


}