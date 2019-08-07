package com.ronaker.app.ui.orderPreview

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
import com.ronaker.app.utils.AnimationHelper

class OrderPreviewActivity : BaseActivity() {

    private val TAG = OrderPreviewActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderPreviewBinding
    private lateinit var viewModel: OrderPreviewViewModel



    companion object {
        var SUID_KEY = "suid"

        fun newInstance(context: Context,suid:String): Intent {
            var intent = Intent(context, OrderPreviewActivity::class.java)
            var boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_preview)

        viewModel = ViewModelProviders.of(this).get(OrderPreviewViewModel::class.java)

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


        binding.toolbar.cancelClickListener= View.OnClickListener {

            finishSafe()
        }



    }




    fun getSuid():String?
    {
        if ( intent.hasExtra(SUID_KEY)) {
            var value = intent.getStringExtra(SUID_KEY)

            return value

        }
        return null
    }







}