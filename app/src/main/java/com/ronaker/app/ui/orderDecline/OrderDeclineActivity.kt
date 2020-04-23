package com.ronaker.app.ui.orderDecline

import android.app.Activity
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
import com.ronaker.app.model.Order

class OrderDeclineActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderDeclineBinding
    private lateinit var viewModel: OrderDeclineViewModel


    companion object {
        var Order_KEY = "order"

        var REQUEST_CODE = 351
        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, OrderDeclineActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_decline)

        viewModel = ViewModelProvider(this).get(OrderDeclineViewModel::class.java)

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




        viewModel.finish.observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finish()
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finish()
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