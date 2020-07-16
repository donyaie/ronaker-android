package com.ronaker.app.ui.orderStartRenting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ronaker.app.utils.Alert
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order

class OrderStartRentingActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderStartRentingBinding
    private lateinit var viewModel: OrderStartRentingViewModel



    companion object {
        var Order_KEY = "order"

        var REQUEST_CODE = 352

        fun newInstance(context: Context,order: Order?): Intent {
            val intent = Intent(context, OrderStartRentingActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_start_renting)

        viewModel = ViewModelProvider(this).get(OrderStartRentingViewModel::class.java)

        binding.viewModel = viewModel

        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.recyclerView,false)


        binding.cardRecyclerView.layoutManager= LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.cardRecyclerView,false)



        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })

        viewModel.finish.observe(this, Observer { _ ->
            setResult(Activity.RESULT_OK)
           finish()
        })


        binding.acceptButton.isEnabled=false
        binding.acceptTerm.setOnCheckedChangeListener { _, isChecked ->
            binding.acceptButton.isEnabled=isChecked
        }


        binding.toolbar.cancelClickListener= View.OnClickListener {

            finish()
        }


        getOrder()?.let { viewModel.load(it) }



    }




    private fun getOrder():Order?
    {
        if ( intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
    }







}