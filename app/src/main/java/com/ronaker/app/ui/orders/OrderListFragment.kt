package com.ronaker.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener

class OrderListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderListBinding
    private lateinit var viewModel: OrderListViewModel


    private  var scrollListener: EndlessRecyclerViewScrollListener?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false)
        viewModel = ViewModelProviders.of(this).get(OrderListViewModel::class.java)

//
        binding.viewModel = viewModel
        val mnager = LinearLayoutManager(context)
        binding.recycler.layoutManager = mnager


        viewModel.loading.observe(this, Observer { loading ->
            binding.refreshLayout.isRefreshing = loading
        })
        viewModel.retry.observe(this, Observer { loading ->

            loading?.let {   binding.loading.showRetry(it) }?:run{binding.loading.hideRetry()}
        })



        binding.loading.hideLoading()

        viewModel.resetList.observe(this, Observer {
            scrollListener?.resetState()
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null)
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()

        }


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }





        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadData(getFilter())

    }


    private fun getFilter(): String? {
        return this.arguments?.getString(FILTER_KEY)
    }

    companion object {

        private var FILTER_KEY = "filter"
        fun newInstance(filter: String?): OrderListFragment {

            val bundle = Bundle()
            bundle.putString(FILTER_KEY, filter)
            val fragment = OrderListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}