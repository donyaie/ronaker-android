package com.ronaker.app.ui.orderFinish

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFinishActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderFinishBinding
    private val viewModel: OrderFinishViewModel by viewModels()


    companion object {
        var Order_KEY = "order"

        var REQUEST_CODE = 353

        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, OrderFinishActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_finish)


        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })




        viewModel.loading.observe(this, {value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })




        viewModel.finish.observe(this, {
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