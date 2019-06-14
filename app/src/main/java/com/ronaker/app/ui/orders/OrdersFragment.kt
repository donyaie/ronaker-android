package com.ronaker.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent

class OrdersFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel

  lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        viewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)

        var mnager = LinearLayoutManager(context )
        binding.recycler.layoutManager = mnager


        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })


        viewModel.resetList.observe(this, Observer {
            scrollListener.resetState()
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                binding.loading.showRetry()
            } else {
//                binding.loading.hideRetry()
            }
        })

        binding.loading.oClickRetryListener = object : LoadingComponent.OnClickRetryListener {
            override fun onClick() {
                viewModel.loadProduct()
            }

        }
//
        binding.viewModel = viewModel


        return binding.root
    }


    companion object {

        fun newInstance(): OrdersFragment {
            return OrdersFragment()
        }
    }


}