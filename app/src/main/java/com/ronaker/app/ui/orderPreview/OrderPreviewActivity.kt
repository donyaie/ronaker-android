package com.ronaker.app.ui.orderPreview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.ui.orderAcceptIntro.OrderAcceptActivity
import com.ronaker.app.ui.orderDecline.OrderDeclineActivity
import com.ronaker.app.ui.orderFinish.OrderFinishActivity
import com.ronaker.app.ui.orderStartRenting.OrderStartRentingActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.finishSafe
import com.ronaker.app.utils.extension.startActivityMakeSceneForResult

class OrderPreviewActivity : BaseActivity() {

    private val TAG = OrderPreviewActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderPreviewBinding
    private lateinit var viewModel: OrderPreviewViewModel


    companion object {
        var Order_KEY = "order"

        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, OrderPreviewActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
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



        viewModel.showProduct.observe(this, Observer { product ->
            startActivityMakeSceneForResult(
                ExploreProductActivity.newInstance(this, product, "ds"),
                ExploreProductActivity.REQUEST_CODE
            )
        })


        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.acceptIntro.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderAcceptActivity.newInstance(this, getOrder()),
                OrderAcceptActivity.REQUEST_CODE
            )
        })

        viewModel.declineIntro.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderDeclineActivity.newInstance(this, getOrder()),
                OrderDeclineActivity.REQUEST_CODE
            )
        })

        viewModel.cancelDialog.observe(this, Observer {
            showCancel()
        })
        viewModel.startRenting.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderStartRentingActivity.newInstance(this, getOrder()),
                OrderStartRentingActivity.REQUEST_CODE
            )
        })


        viewModel.finishIntro.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderFinishActivity.newInstance(this, getOrder()),
                OrderFinishActivity.REQUEST_CODE
            )
        })



        viewModel.finish.observe(this, Observer {
            finishSafe()
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finishSafe()
        }


        getOrder()?.let { viewModel.load(it) }


    }


    private fun showCancel() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to cancel this order?")
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            dialog?.cancel()

            viewModel.canceled()
        }
        builder.setNegativeButton(getString(android.R.string.cancel))
        { dialog, _ ->
            dialog?.cancel()
        }
        builder.show()
    }


    private fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        when (requestCode) {

            OrderAcceptActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    finishSafe()
                }
            }


            OrderDeclineActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    finishSafe()
                }
            }


            OrderStartRentingActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    finishSafe()
                }
            }


            OrderFinishActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    finishSafe()
                }
            }


        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}