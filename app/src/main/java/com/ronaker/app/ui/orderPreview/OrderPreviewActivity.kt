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
import com.ronaker.app.ui.orderMessage.OrderMessageActivity
import com.ronaker.app.ui.orderStartRenting.OrderStartRentingActivity
import com.ronaker.app.utils.AnimationHelper

class OrderPreviewActivity : BaseActivity() {

    private val TAG = OrderPreviewActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderPreviewBinding
    private lateinit var viewModel: OrderPreviewViewModel


    companion object {
        var Order_KEY = "order"

        fun newInstance(context: Context, order: Order?): Intent {
            var intent = Intent(context, OrderPreviewActivity::class.java)
            var boundle = Bundle()
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



        viewModel.acceptIntro.observe(this, Observer { _ ->
            startActivityMakeSceneForResult(
                OrderAcceptActivity.newInstance(this, getOrder()),
                OrderAcceptActivity.REQUEST_CODE
            )
        })

        viewModel.declineIntro.observe(this, Observer { _ ->
            startActivityMakeSceneForResult(
                OrderDeclineActivity.newInstance(this, getOrder()),
                OrderDeclineActivity.REQUEST_CODE
            )
        })

        viewModel.cancelDialog.observe(this, Observer { _ ->
            showCancel()
        })
        viewModel.startRenting.observe(this, Observer { _ ->
            startActivityMakeSceneForResult(
                OrderStartRentingActivity.newInstance(this, getOrder()),
                OrderStartRentingActivity.REQUEST_CODE
            )
        })


        viewModel.finishIntro.observe(this, Observer { _ ->
            startActivityMakeSceneForResult(
                OrderFinishActivity.newInstance(this, getOrder()),
                OrderFinishActivity.REQUEST_CODE
            )
        })



        viewModel.finish.observe(this, Observer { _ ->
            finishSafe()
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finishSafe()
        }


        getOrder()?.let { viewModel.load(it) }


    }


    private fun showCancel() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
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


    fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {
            var value = intent.getParcelableExtra<Order?>(Order_KEY)

            return value

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