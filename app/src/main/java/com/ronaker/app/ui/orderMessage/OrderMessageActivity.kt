package com.ronaker.app.ui.orderMessage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.addProduct.AddProductCategorySelectDialog
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.IntentManeger
import java.util.*

class OrderMessageActivity : BaseActivity() {

    private val TAG = OrderMessageActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderMessageBinding
    private lateinit var viewModel: OrderMessageViewModel

    companion object {
        var PRODUCT_KEY = "mProduct"
        var STARTDATE_KEY = "start_date"
        var ENDDATE_KEY = "end_date"


        var REQUEST_CODE = 347

        fun newInstance(
            context: Context,
            product: Product?,
            startDate: Date?,
            endDate: Date?
        ): Intent {
            var intent = Intent(context, OrderMessageActivity::class.java)
            var boundle = Bundle()

            if (endDate != null && startDate != null) {

                boundle.putParcelable(PRODUCT_KEY, product)
                boundle.putLong(STARTDATE_KEY, startDate.time)

                boundle.putLong(ENDDATE_KEY, endDate.time)
            }


            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_message)

        viewModel = ViewModelProviders.of(this).get(OrderMessageViewModel::class.java)

        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        viewModel.next.observe(this, Observer { _ ->
           finishSafe()
        })


        viewModel.successMessage.observe(this, Observer { _ ->
           succeccSend()
        })


        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        if (intent.hasExtra(PRODUCT_KEY)) {


            getProduct()?.let { viewModel.loadProduct(it,getStartDate(),getEndDate()) }


        } else {
            finishSafe()
        }


    }


    fun getProduct(): Product? {
        return intent.getParcelableExtra(PRODUCT_KEY)
    }


    fun getStartDate(): Date {


        return Date(intent.getLongExtra( STARTDATE_KEY,-1))
    }


    fun getEndDate(): Date {
        return Date(intent.getLongExtra( ENDDATE_KEY,-1))
    }


    fun succeccSend(){
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.text_your_request_sent))
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            dialog?.cancel()
            setResult(Activity.RESULT_OK)
            finishSafe()

//            startActivity(DashboardActivity.newInstance(this))

        }
        builder.setCancelable(false)

        builder.show()
    }


}