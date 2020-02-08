package com.ronaker.app.ui.chackoutCalendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ronaker.app.utils.Alert
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.finishSafe

class CheckoutCalendarActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityCheckoutCalendarBinding
    private lateinit var viewModel: CheckoutCalendarViewModel

    companion object {

        private  val TAG = CheckoutCalendarActivity::class.java.simpleName
        const val PRODUCT_KEY = "mProduct"
        const val STARTDATE_KEY = "start_date"
        const val ENDDATE_KEY = "end_date"
        const val REQUEST_CODE = 346

        fun newInstance(context: Context, product: Product): Intent {
            val intent = Intent(context, CheckoutCalendarActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_calendar)

        viewModel = ViewModelProvider(this).get(CheckoutCalendarViewModel::class.java)

        binding.viewModel = viewModel


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, CheckoutCalendarFragment())
            .commit()





        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
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


}