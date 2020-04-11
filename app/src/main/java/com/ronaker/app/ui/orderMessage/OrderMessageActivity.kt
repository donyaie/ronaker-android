package com.ronaker.app.ui.orderMessage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ronaker.app.utils.Alert
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import java.util.*

class OrderMessageActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderMessageBinding
    private lateinit var viewModel: OrderMessageViewModel



    companion object {

        var PRODUCT_KEY = "mProduct"
        var STARTDATE_KEY = "start_date"
        var ENDDATE_KEY = "end_date"


        var REQUEST_CODE = 347



        fun newInstance(context: Context,
                        product: Product?,
                        startDate: Date?,
                        endDate: Date?): Intent {
            val intent = Intent(context, OrderMessageActivity::class.java)
            val boundle = Bundle()
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


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_message)

        viewModel = ViewModelProvider(this).get(OrderMessageViewModel::class.java)

        binding.viewModel = viewModel


        binding.toolbar.actionTextClickListener = View.OnClickListener {

        }



        binding.toolbar.cancelClickListener = View.OnClickListener {

           finish()
        }





        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.next.observe(this, Observer {
            this.finish()
        })


        viewModel.goNext.observe(this, Observer {
            this.startActivity(
                ProfileCompleteActivity.newInstance(
                    this
                )
            )
        })


        viewModel.successMessage.observe(this, Observer {
            succeccSend()
        })




        getProduct()?.let { viewModel.loadProduct(it, getStartDate(), getEndDate()) }



    }


    fun getProduct(): Product? {
        return intent?.getParcelableExtra(PRODUCT_KEY)
    }


    private fun getStartDate(): Date {


        return Date(intent?.getLongExtra(STARTDATE_KEY, -1) ?: -1)
    }


    private fun getEndDate(): Date {
        return Date(intent?.getLongExtra(ENDDATE_KEY, -1) ?: -1)
    }

    private fun succeccSend() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.text_your_request_sent))
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            dialog?.cancel()
            this.setResult(Activity.RESULT_OK)
            this.finish()


        }
        builder.setCancelable(false)

        builder.show()
    }




}