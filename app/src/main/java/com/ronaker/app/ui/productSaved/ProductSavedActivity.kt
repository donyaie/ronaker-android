package com.ronaker.app.ui.productSaved

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ronaker.app.utils.Alert
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener


class ProductSavedActivity : BaseActivity() {

    private val TAG = ProductSavedActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductSavedBinding
    private lateinit var viewModel: ProductSavedViewModel


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

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_saved)

        viewModel = ViewModelProvider(this).get(ProductSavedViewModel::class.java)

        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if(errorMessage!=null) Alert.makeTextError(this, errorMessage)
        })




        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        //-------------------------


        val mnager = GridLayoutManager(this, 2)
        binding.recycler.layoutManager = mnager
        binding.loading.hideLoading()

        viewModel.loading.observe(this, Observer { loading ->
            binding.refreshLayout.isRefreshing = loading

        })
        viewModel.retry.observe(this, Observer { loading ->
            loading?.let {   binding.loading.showRetry(it) }?:run{binding.loading.hideRetry()}

        })


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }


        viewModel.resetList.observe(this, Observer {
            scrollListener.resetState()
        })




        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()


        }
//
        scrollListener = object : EndlessRecyclerViewScrollListener(mnager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {

                viewModel.loadMore()


            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)




            }
        }
        binding.recycler.addOnScrollListener(scrollListener)


    }

    override fun onStart() {
        super.onStart()
        viewModel.retry()
    }






}