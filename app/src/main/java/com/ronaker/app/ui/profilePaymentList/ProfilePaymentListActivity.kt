package com.ronaker.app.ui.profilePaymentList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.profilePayment.ProfilePaymentActivity
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfilePaymentListActivity : BaseActivity() {

//    private val TAG = ProfileSettingActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfilePaymentListBinding
    private val viewModel: ProfilePaymentListViewModel by viewModels()


    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfilePaymentListActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_payment_list)

        binding.viewModel = viewModel


        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)

        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, {value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.retry.observe(this, {value ->

            if(value.isNullOrEmpty()){
                binding.loading.hideRetry()
            }else{
                binding.loading.showRetry(value)
            }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }



        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


        binding.newLayout.setOnClickListener {


            startActivity(ProfilePaymentActivity.newInstance(this))
        }


    }

    override fun onStart() {

        super.onStart()
//
//        if (isFistStart()) {

        viewModel.loadData()


//        }


    }


}