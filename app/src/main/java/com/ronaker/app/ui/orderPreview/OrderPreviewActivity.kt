package com.ronaker.app.ui.orderPreview

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.docusignSign.DocusignSignActivity
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.ui.orderAccept.OrderAcceptActivity
import com.ronaker.app.ui.orderAuthorization.OrderAuthorizationActivity
import com.ronaker.app.ui.orderCancel.OrderCancelActivity
import com.ronaker.app.ui.orderDecline.OrderDeclineActivity
import com.ronaker.app.ui.orderFinish.OrderFinishActivity
import com.ronaker.app.ui.orderStartRenting.OrderStartRentingActivity
import com.ronaker.app.ui.productRate.ProductRateActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.IntentManeger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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


        fun newInstance(context: Activity, orderId: String): Intent {
            val intent = Intent(context, OrderPreviewActivity::class.java)
            val boundle = Bundle()
            boundle.putString(OrderId_KEY, orderId)
            intent.putExtras(boundle)

            return intent
        }


        fun newInstance(context: Application, orderId: String): Intent {
            val intent = Intent(context, OrderPreviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            val boundle = Bundle()
            boundle.putString(OrderId_KEY, orderId)
            intent.putExtras(boundle)

            return intent
        }
    }


    override fun onNewIntent(newIntent: Intent?) {
        newIntent?.let {
            val orderId = getCurrentOrderId(intent)
            val newOrderId = getCurrentOrderId(newIntent)


            if (orderId != newOrderId) {

                val intent = Intent(this, OrderPreviewActivity::class.java)

                intent.putExtras(newIntent)

                this.finish()
                startActivity(intent)
            } else if (orderId != null) {
                try {
                    viewModel.getOrder(orderId)
                } catch (ex: Exception) {

                }
            }
        }

        super.onNewIntent(newIntent)


    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_preview)

        viewModel = ViewModelProvider(this).get(OrderPreviewViewModel::class.java)

        binding.viewModel = viewModel


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.recyclerView, false)

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)


        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })


        binding.refreshLayout.setOnRefreshListener {

            getCurrentOrderId(intent)?.let {
                viewModel.getOrder(it)
            }
        }


        viewModel.showProduct.observe(this, {product ->
            startActivityForResult(
                ExploreProductActivity.newInstance(this, product),
                ExploreProductActivity.REQUEST_CODE
            )


        })

        viewModel.makeCall.observe(this, {value ->

            IntentManeger.makeCall(this@OrderPreviewActivity, value)


        })

        viewModel.sendEmail.observe(this, {value ->

            IntentManeger.sendMail(this@OrderPreviewActivity, value)

        })


        viewModel.loading.observe(this, {value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else {
                binding.loading.hideLoading()
                binding.refreshLayout.isRefreshing=false
            }
        })



        viewModel.acceptIntro.observe(this, {
            startActivityForResult(
                OrderAcceptActivity.newInstance(this, viewModel.getOrder()),
                OrderAcceptActivity.REQUEST_CODE
            )
        })

        viewModel.declineIntro.observe(this, {
            startActivityForResult(
                OrderDeclineActivity.newInstance(this, viewModel.getOrder()),
                OrderDeclineActivity.REQUEST_CODE
            )
        })

        viewModel.cancelDialog.observe(this, {

            startActivityForResult(
                OrderCancelActivity.newInstance(this, viewModel.getOrder()),
                OrderCancelActivity.REQUEST_CODE
            )
        })


        viewModel.signContractShow.observe(this, {

            viewModel.getOrder()?.suid.let {

                startActivity(DocusignSignActivity.newInstance(this@OrderPreviewActivity,it))

            }


//            startActivityForResult(
//                OrderAuthorizationActivity.newInstance(this, viewModel.getOrder()),
//                OrderAuthorizationActivity.REQUEST_CODE
//            )
        })

        viewModel.contractPreview.observe(this, {

            startActivityForResult(
                OrderAuthorizationActivity.newInstance(this, viewModel.getOrder(),false),
                OrderAuthorizationActivity.REQUEST_CODE
            )
        })



        viewModel.previewContractShow.observe(this, {file ->

            file?.let {

                IntentManeger.openPDF(this, it)
            }

        })





        viewModel.startRenting.observe(this, {
            startActivityForResult(
                OrderStartRentingActivity.newInstance(this, viewModel.getOrder()),
                OrderStartRentingActivity.REQUEST_CODE
            )
        })



        viewModel.startRate.observe(this, {
            startActivityForResult(
                ProductRateActivity.newInstance(this, viewModel.getOrder()),
                ProductRateActivity.REQUEST_CODE
            )
        })


        viewModel.finishIntro.observe(this, {
            startActivityForResult(
                OrderFinishActivity.newInstance(this, viewModel.getOrder()),
                OrderFinishActivity.REQUEST_CODE
            )
        })



        viewModel.finish.observe(this, {
            finish()
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finish()
        }


//        getOrder()?.let { viewModel.load(it) }


    }

    override fun onStart() {
        super.onStart()

        if (isFistStart() && getOrder() != null) {
            getOrder()?.let {

                viewModel.load(it)
                viewModel.getOrder(it.suid)

            }

        } else {
            getCurrentOrderId(intent)?.let {
                viewModel.getOrder(it)
            }
        }


    }


    private fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
    }


    private fun getCurrentOrderId(intent: Intent?): String? {

        var orderId: String? = null
        if (intent?.hasExtra(OrderId_KEY) == true) {

            orderId = intent.getStringExtra(OrderId_KEY)

        } else if (intent?.hasExtra(Order_KEY) == true) {

            orderId = intent.getParcelableExtra<Order?>(Order_KEY)?.suid

        }


        return orderId
    }

    override fun onScrollChanged() {

        binding.toolbar.isBottomLine = binding.scrollView.canScrollVertically(-1)
    }


}