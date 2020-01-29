package com.ronaker.app.ui.orderPreview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.ronaker.app.utils.Alert
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.ui.orderAcceptIntro.OrderAcceptActivity
import com.ronaker.app.ui.orderCancel.OrderCancelActivity
import com.ronaker.app.ui.orderDecline.OrderDeclineActivity
import com.ronaker.app.ui.orderFinish.OrderFinishActivity
import com.ronaker.app.ui.orderStartRenting.OrderStartRentingActivity
import com.ronaker.app.ui.productRate.ProductRateActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.finishSafe
import com.ronaker.app.utils.extension.startActivityMakeSceneForResult

class OrderPreviewActivity : BaseActivity(), ViewTreeObserver.OnScrollChangedListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderPreviewBinding
    private lateinit var viewModel: OrderPreviewViewModel


    companion object {
        var Order_KEY = "order"
        var OrderId_KEY = "orderSuid"

        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, OrderPreviewActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }


        fun newInstance(context: Context, orderId:String): Intent {
            val intent = Intent(context, OrderPreviewActivity::class.java)
            val boundle = Bundle()
            boundle.putString(OrderId_KEY, orderId)
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


        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.recyclerView,false)

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.showProduct.observe(this, Observer { product ->
            startActivityMakeSceneForResult(
                ExploreProductActivity.newInstance(this, product, "ds"),
                ExploreProductActivity.REQUEST_CODE
            )
        })


        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.visibility=View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.acceptIntro.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderAcceptActivity.newInstance(this,viewModel. getOrder()),
                OrderAcceptActivity.REQUEST_CODE
            )
        })

        viewModel.declineIntro.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderDeclineActivity.newInstance(this, viewModel.getOrder()),
                OrderDeclineActivity.REQUEST_CODE
            )
        })

        viewModel.cancelDialog.observe(this, Observer {

            startActivityMakeSceneForResult(
                OrderCancelActivity.newInstance(this, viewModel.getOrder()),
                OrderCancelActivity.REQUEST_CODE
            )
        })



        viewModel.startRenting.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderStartRentingActivity.newInstance(this, viewModel.getOrder()),
                OrderStartRentingActivity.REQUEST_CODE
            )
        })



        viewModel.startRate.observe(this, Observer {
            startActivityMakeSceneForResult(
                ProductRateActivity.newInstance(this, viewModel.getOrder()),
                ProductRateActivity.REQUEST_CODE
            )
        })


        viewModel.finishIntro.observe(this, Observer {
            startActivityMakeSceneForResult(
                OrderFinishActivity.newInstance(this,viewModel.getOrder()),
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

//            ?: kotlin.run {
//
//            getOrderId()?.let{ ordreId->viewModel.getOrder(ordreId) }
//
//        }


    }

    override fun onStart() {
        super.onStart()
//        getOrder()?.suid?.let {
//
//
//            viewModel.getOrder(it)
//        }



        getOrder()?.let { viewModel.getOrder(it.suid) }?: kotlin.run {

            getOrderId()?.let{ ordreId->viewModel.getOrder(ordreId) }

        }


    }



    private fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
    }


    private fun getOrderId(): String? {
        if (intent.hasExtra(OrderId_KEY)) {

            return intent.getStringExtra(OrderId_KEY)

        }
        return null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


//        when (requestCode) {
//
//            OrderAcceptActivity.REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    finishSafe()
//                }
//            }
//
//            ProductRateActivity.REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    finishSafe()
//                }
//            }
//
//
//            OrderDeclineActivity.REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    finishSafe()
//                }
//            }
//
//
//            OrderStartRentingActivity.REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    finishSafe()
//                }
//            }
//
//
//            OrderFinishActivity.REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    finishSafe()
//                }
//            }
//
//            OrderCancelActivity.REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    finishSafe()
//                }
//            }
//


//        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onScrollChanged() {

        binding.toolbar.isBottomLine = binding.scrollView.canScrollVertically(-1)
    }


}