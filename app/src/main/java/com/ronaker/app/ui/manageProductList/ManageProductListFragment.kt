package com.ronaker.app.ui.manageProductList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent

class ManageProductListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentManageProductListBinding
    private lateinit var viewModel: ManageProductListViewModel

    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_product_list, container, false)
        viewModel = ViewModelProviders.of(this).get(ManageProductListViewModel::class.java)

        var mnager = GridLayoutManager(context, 2)
        binding.recycler.layoutManager = mnager


        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })

        viewModel.emptyView.observe(this, Observer { loading ->
            if (loading) {
                binding.emptyLayout.visibility = View.VISIBLE
                binding.addNewProductButton.visibility = View.GONE
            } else {
                binding.emptyLayout.visibility = View.GONE
                binding.addNewProductButton.visibility = View.VISIBLE
            }
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
        binding.viewModel = viewModel


        return binding.root
    }


    companion object {

        fun newInstance(): ManageProductListFragment {
            return ManageProductListFragment()
        }
    }


}