package com.ronaker.app.ui.chackoutCalendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.utils.AnimationHelper

class CheckoutCalendarActivity : BaseActivity() {

    private val TAG = CheckoutCalendarActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityCheckoutCalendarBinding
    private lateinit var viewModel: CheckoutCalendarViewModel

    companion object {
        var PRODUCT_KEY = "mProduct"

        var STARTDATE_KEY = "start_date"
        var ENDDATE_KEY = "end_date"

        var REQUEST_CODE = 346

        fun newInstance(context: Context, product: Product): Intent {
            var intent = Intent(context, CheckoutCalendarActivity::class.java)
            var boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_calendar)

        viewModel = ViewModelProviders.of(this).get(CheckoutCalendarViewModel::class.java)

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



        if (intent.hasExtra(PRODUCT_KEY)) {


            viewModel.loadProduct()


        } else {
            finishSafe()
        }


    }


    fun getProduct(): Product? {
        return intent.getParcelableExtra(PRODUCT_KEY)
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


}