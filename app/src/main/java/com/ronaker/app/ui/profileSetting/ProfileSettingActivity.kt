package com.ronaker.app.ui.profileSetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.language.LanguageDialog
import com.ronaker.app.ui.profilePaymentList.ProfilePaymentListActivity
import com.ronaker.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import io.branch.referral.Branch


@AndroidEntryPoint
class ProfileSettingActivity : BaseActivity() {

//    private val TAG = ProfileSettingActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileSettingBinding
    private val viewModel: ProfileSettingViewModel by viewModels()


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileSettingActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_setting)


        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.retry.observe(this, { value ->

            value?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }



        binding.logoutLayout.setOnClickListener {
            showLogoutDialog()

        }
        binding.downloadLayout.setOnClickListener {

        }

        binding.paymentLayout.setOnClickListener {
          startActivity(  ProfilePaymentListActivity.newInstance(this))

        }

        binding.languageLayout.setOnClickListener {
            LanguageDialog.showDialog(this)
        }


        binding.licenseLayout.setOnClickListener {

            IntentManeger.openUrl(this, viewModel.getTermUrl())
        }

        binding.privacyLayout.setOnClickListener {

            IntentManeger.openUrl(this, viewModel.getPrivacyUrl())
        }



        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.versionText.text = String.format(
            getString(R.string.versionNameFormat),
            getString(R.string.app_version)
        )  //  "--Version V${BuildConfig.VERSION_NAME}--"


    }

    override fun onStart() {

        super.onStart()
        viewModel.loadData()


    }


    private fun showLogoutDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.text_are_you_sure))
        builder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog?.cancel()
            viewModel.logout()
            GoogleSignManger.getClient(this).signOut()
            Branch.getInstance().logout()
            startActivity(DashboardActivity.newInstance(this))
            AnimationHelper.setFadeTransition(this)
        }
        builder.setNegativeButton(getString(android.R.string.cancel))
        { dialog, _ -> dialog?.cancel() }
        builder.show()
    }


}