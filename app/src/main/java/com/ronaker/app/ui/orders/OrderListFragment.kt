package com.ronaker.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity
import com.ronaker.app.ui.productRate.ProductRateActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderListBinding

    private val viewModel: OrderListViewModel by viewModels()

    private var scrollListener: EndlessRecyclerViewScrollListener? = null


    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var pastVisiblesItems: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false)

        binding.viewModel = viewModel

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mnager = LinearLayoutManager(context, RecyclerView.VERTICAL, false).apply {
        }


        val orderAdapter = OrderItemAdapter(viewModel)

        viewModel.setFilter(getFilter())

        binding.recycler.adapter=orderAdapter
        binding.recycler.isMotionEventSplittingEnabled = true

        binding.recycler.layoutManager = mnager
        binding.recycler.setHasFixedSize(false)
        viewModel.loading.observe(viewLifecycleOwner, { loading ->

            binding.refreshLayout.isRefreshing = loading
        })

        viewModel.retry.observe(viewLifecycleOwner, { loading ->

            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }
        })



        binding.loading.hideLoading()

        viewModel.resetList.observe(viewLifecycleOwner, {
            scrollListener?.resetState()
        })


        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null)
                Alert.makeTextError(this, errorMessage)
//
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()

        }

        viewModel.launchOrderDetail.observe(viewLifecycleOwner, { order ->

            startActivity(OrderPreviewActivity.newInstance(requireContext(), order))
        })


        viewModel.listView.observe(viewLifecycleOwner, {

           orderAdapter.submitList(it.toList())
        })

        viewModel.launchOrderRateDetail.observe(viewLifecycleOwner, { order ->

            startActivity(ProductRateActivity.newInstance(requireContext(), order))
        })


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }





        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->


            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    visibleItemCount = mnager.childCount
                    totalItemCount = mnager.itemCount
                    pastVisiblesItems = mnager.findFirstVisibleItemPosition()

                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {

                        viewModel.getData()
                    }

                }


            }


        })

    }


    override fun onStart() {
        super.onStart()

        viewModel.retry()

//        viewModel.getData(getFilter())
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