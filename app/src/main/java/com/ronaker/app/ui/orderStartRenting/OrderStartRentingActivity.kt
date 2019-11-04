package com.ronaker.app.ui.orderStartRenting

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

class OrderStartRentingActivity : BaseActivity() {

    private val TAG = OrderStartRentingActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderStartRentingBinding
    private lateinit var viewModel: OrderStartRentingViewModel



    companion object {
        var Order_KEY = "order"

        fun newInstance(context: Context,order: Order?): Intent {
            var intent = Intent(context, OrderStartRentingActivity::class.java)
            var boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_start_renting)

        viewModel = ViewModelProviders.of(this).get(OrderStartRentingViewModel::class.java)

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




        viewModel.finish.observe(this, Observer { _ ->
           finishSafe()
        })


        binding.toolbar.cancelClickListener= View.OnClickListener {

            finishSafe()
        }


        getOrder()?.let { viewModel.load(it) }



    }




    fun getOrder():Order?
    {
        if ( intent.hasExtra(Order_KEY)) {
            var value = intent.getParcelableExtra<Order?>(Order_KEY)

            return value

        }
        return null
    }







}