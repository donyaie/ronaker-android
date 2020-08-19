package com.ronaker.app.ui.productRate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductRateActivity : BaseActivity() {

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductRateBinding
    private val viewModel: ProductRateViewModel by viewModels()

    companion object {

        var REQUEST_CODE = 358
        var Order_KEY = "order"

        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, ProductRateActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)


            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_rate)

        binding.viewModel = viewModel
        binding.starRate.rating





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