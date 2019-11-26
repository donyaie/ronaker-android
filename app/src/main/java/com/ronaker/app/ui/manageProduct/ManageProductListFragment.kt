package com.ronaker.app.ui.manageProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.addProduct.AddProductActivity
import com.ronaker.app.utils.extension.startActivityMakeScene
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener

class ManageProductListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentManageProductListBinding
    private lateinit var viewModel: ManageProductListViewModel

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_manage_product_list,
            container,
            false
        )
        viewModel = ViewModelProviders.of(this).get(ManageProductListViewModel::class.java)

        binding.viewModel = viewModel

        binding.loading.hideLoading()


        binding.recycler.layoutManager = GridLayoutManager(context, 2)
//        binding.recycler.setOnTouchListener(View.OnTouchListener { v, event -> true })

        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)

        viewModel.loading.observe(this, Observer { loading ->
            binding.refreshLayout.isRefreshing = loading
        })



        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }


        viewModel.retry.observe(this, Observer { loading ->
            loading?.let { binding.loading.showRetry(loading)  }?:run{binding.loading.hideRetry()}


        })

        viewModel.emptyView.observe(this, Observer { loading ->
            if (loading) {
                binding.emptyLayout.visibility = View.VISIBLE
            } else {
                binding.emptyLayout.visibility = View.GONE
            }
        })

        viewModel.addNewView.observe(this, Observer { loading ->
            if (loading) {

                binding.addNewProductButton.visibility = View.VISIBLE
                binding.addNewProductButton.isClickable = true
            } else {


                binding.addNewProductButton.visibility = View.INVISIBLE
                binding.addNewProductButton.isClickable = false
            }
        })




        binding.addNewProductButton.setOnClickListener {
            activity?.startActivityMakeScene(activity?.let { it1 -> AddProductActivity.newInstance(it1) })
        }
        binding.addProductButton.setOnClickListener {
            activity?.startActivityMakeScene(activity?.let { it1 -> AddProductActivity.newInstance(it1) })
        }

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()


        }

        viewModel.resetList.observe(this, Observer {
            scrollListener.resetState()
        })


        scrollListener = object :
            EndlessRecyclerViewScrollListener(binding.recycler.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {

                viewModel.loadMore()


            }

        }
        binding.recycler.addOnScrollListener(scrollListener)





        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.retry()
    }

    override fun onDetach() {
        try {
            binding.recycler.viewTreeObserver
                .removeOnScrollChangedListener(scrollListener as ViewTreeObserver.OnScrollChangedListener)
        } catch (e: Exception) {

        }
        super.onDetach()
    }


    companion object {

        fun newInstance(): ManageProductListFragment {
            return ManageProductListFragment()
        }
    }


}