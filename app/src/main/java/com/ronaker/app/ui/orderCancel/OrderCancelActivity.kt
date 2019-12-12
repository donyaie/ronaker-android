package com.ronaker.app.ui.orderCancel

import android.app.Activity
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
import com.ronaker.app.model.Order
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.finishSafe

class OrderCancelActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderCancelBinding
    private lateinit var viewModel: OrderCancelViewModel


    companion object {
        var Order_KEY = "order"

        var REQUEST_CODE = 355
        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, OrderCancelActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_cancel)

        viewModel = ViewModelProviders.of(this).get(OrderCancelViewModel::class.java)

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




        viewModel.finish.observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finishSafe()
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finishSafe()
        }


        getOrder()?.let { viewModel.load(it) }


    }


    private fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
    }


}