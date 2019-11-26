package com.ronaker.app.ui.profileSetting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.BuildConfig
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.language.LanguageDialog
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.extension.startActivityMakeScene


class ProfileSettingActivity : BaseActivity() {

//    private val TAG = ProfileSettingActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileSettingBinding
    private lateinit var viewModel: ProfileSettingViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileSettingActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_setting)

        viewModel = ViewModelProviders.of(this).get(ProfileSettingViewModel::class.java)

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
            
            value?.let {   binding.loading.showRetry(it) }?:run{binding.loading.hideRetry()}
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }



        binding.logoutLayout.setOnClickListener{
         showLogoutDialog()

        }
        binding.downloadLayout.setOnClickListener{

        }


        binding.languageLayout.setOnClickListener{
            LanguageDialog.showDialog(this)
        }


        binding.termsLayout.setOnClickListener {

            IntentManeger.openUrl(this,"https://ronaker.com/?page_id=7678")
        }



        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.versionText.text = "--Version V${BuildConfig.VERSION_NAME}--"



    }

    override fun onStart() {

        super.onStart()
//
//        if (isFistStart()) {

           viewModel.loadData()


//        }


    }




    private fun showLogoutDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.text_are_you_sure))
        builder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog?.cancel()
            viewModel.logout()
            startActivityMakeScene(DashboardActivity.newInstance(this) )
        }
        builder.setNegativeButton(getString(android.R.string.cancel))
        { dialog, _ -> dialog?.cancel() }
        builder.show()
    }


}