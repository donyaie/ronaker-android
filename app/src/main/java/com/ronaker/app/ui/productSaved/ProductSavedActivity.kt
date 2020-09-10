package com.ronaker.app.ui.productSaved

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.explore.ItemExploreAdapter
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductSavedActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProductSavedBinding
    private val viewModel: ProductSavedViewModel by viewModels()



    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProductSavedActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_saved)

        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, { errorMessage ->
            if (errorMessage != null) Alert.makeTextError(this, errorMessage)
        })




        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


        //-------------------------


        val mnager = GridLayoutManager(this, 2)
        binding.recycler.layoutManager = mnager

         val productAdapter=  ItemExploreAdapter()
        binding.recycler.adapter=productAdapter
        binding.loading.hideLoading()

        viewModel.loading.observe(this, { loading ->
            binding.refreshLayout.isRefreshing = loading

        })
        viewModel.retry.observe(this, { loading ->
            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }

        })


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }


        viewModel.resetList.observe(this, {
            scrollListener.resetState()
        })

        viewModel.listView.observe(this, {
           productAdapter.submitList(it.toList())
        })



        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()


        }
//
        scrollListener = object : EndlessRecyclerViewScrollListener(mnager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {

                viewModel.loadMore()


            }

        }
        binding.recycler.addOnScrollListener(scrollListener)


    }

    override fun onStart() {
        super.onStart()
        viewModel.retry()
    }


}