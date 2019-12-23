package com.ronaker.app.ui.profilePaymentHistoryList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ronaker.app.utils.Alert
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.profilePayment.ProfilePaymentActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.startActivityMakeScene


class ProfilePaymentHistoryListActivity : BaseActivity() {

//    private val TAG = ProfileSettingActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfilePaymentHistoryListBinding
    private lateinit var viewModel: ProfilePaymentHistoryListViewModel


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfilePaymentHistoryListActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_payment_history_list)

        viewModel = ViewModelProviders.of(this).get(ProfilePaymentHistoryListViewModel::class.java)

        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
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



        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }





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