package com.ronaker.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity
import com.ronaker.app.ui.productRate.ProductRateActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener

class OrderListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderListBinding
    private lateinit var viewModel: OrderListViewModel


    private var scrollListener: EndlessRecyclerViewScrollListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false)
        viewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)

//
        binding.viewModel = viewModel
        val mnager = LinearLayoutManager(context).apply {
        }
        binding.recycler.layoutManager = mnager


        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            binding.refreshLayout.isRefreshing = loading
        })

        viewModel.retry.observe(viewLifecycleOwner, Observer { loading ->

            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }
        })



        binding.loading.hideLoading()

        viewModel.resetList.observe(viewLifecycleOwner, Observer {
            scrollListener?.resetState()
        })


        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null)
                Alert.makeTextError(this, errorMessage)
//
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()

        }

        viewModel.launchOrderDetail.observe(viewLifecycleOwner, Observer { order ->

            startActivity(OrderPreviewActivity.newInstance(requireContext(), order))
        })

        viewModel.launchOrderRateDetail.observe(viewLifecycleOwner, Observer { order ->

            startActivity(ProductRateActivity.newInstance(requireContext(), order))
        })


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }





        return binding.root
    }


    override fun onStart() {
        super.onStart()

        viewModel.getData(getFilter())
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


        fun newBoundle(filter: String?): Bundle {

            val bundle = Bundle()
            bundle.putString(FILTER_KEY, filter)
            return bundle
        }
    }


}