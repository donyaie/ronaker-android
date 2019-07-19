package com.ronaker.app.ui.manageProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent

class ManageProductListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentManageProductListBinding
    private lateinit var viewModel: ManageProductListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_product_list, container, false)
        viewModel = ViewModelProviders.of(this).get(ManageProductListViewModel::class.java)

        var mnager = GridLayoutManager(context, 2)

        binding.recycler.layoutManager = mnager
//        binding.recycler.setOnTouchListener(View.OnTouchListener { v, event -> true })

        ViewCompat.setNestedScrollingEnabled(binding.recycler,false)

        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })

        viewModel.retry.observe(this, Observer { loading ->
            if (loading) binding.loading.showRetry() else binding.loading.hideRetry()
        })

        viewModel.emptyView.observe(this, Observer { loading ->
            if (loading) {
                binding.emptyLayout.visibility = View.VISIBLE
                binding.addNewProductButton.visibility = View.INVISIBLE
                binding.addNewProductButton.isClickable= false
            } else {
                binding.emptyLayout.visibility = View.GONE
                binding.addNewProductButton.visibility = View.VISIBLE
                binding.addNewProductButton.isClickable= true
            }
        })


        binding.addNewProductButton.setOnClickListener{
            startActivity(activity?.let { it1 -> AddProductActivity.newInstance(it1) })
        }
        binding.addProductButton.setOnClickListener{
            startActivity(activity?.let { it1 -> AddProductActivity.newInstance(it1) })
        }

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                binding.loading.showRetry()
            } else {
//                binding.loading.hideRetry()
            }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

                viewModel.retry()


        }
        binding.viewModel = viewModel


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadProduct()
    }


    companion object {

        fun newInstance(): ManageProductListFragment {
            return ManageProductListFragment()
        }
    }


}