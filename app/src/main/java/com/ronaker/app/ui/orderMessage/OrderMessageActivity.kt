package com.ronaker.app.ui.orderMessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper

class OrderMessageActivity : BaseActivity() {

    private val TAG = OrderMessageActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderMessageBinding
    private lateinit var viewModel: OrderMessageViewModel

    companion object {
        var SUID_KEY = "suid"

        fun newInstance(context: Context, suid: String): Intent {
            var intent = Intent(context, OrderMessageActivity::class.java)
            var boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AnimationHelper.animateActivityFade(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_message)

        viewModel = ViewModelProviders.of(this).get(OrderMessageViewModel::class.java)

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



        if ( intent.hasExtra(SUID_KEY)) {
            var suid = intent.getStringExtra(SUID_KEY)

            viewModel.loadProduct(suid)



        } else {
            finish()
        }


    }





    override fun onBackPressed() {
        finish()
    }





}