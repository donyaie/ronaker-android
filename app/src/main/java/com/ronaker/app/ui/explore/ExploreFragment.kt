package com.ronaker.app.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.post.PostListViewModel
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent

class ExploreFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

  lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)
        viewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)

        var mnager = GridLayoutManager(context, 2)
        binding.recycler.layoutManager = mnager


        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })


        viewModel.resetList.observe(this, Observer { loading ->
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
      scrollListener = object : EndlessRecyclerViewScrollListener(mnager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {

                viewModel.loadMore()


            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)



                if(!view.canScrollVertically(-1)){

                    binding.header.cardElevation=0f
                }else {
                    binding.header.cardElevation = 10f
                }

            }
        };
        binding.recycler.addOnScrollListener(scrollListener)

        binding.viewModel = viewModel


        return binding.root
    }


    companion object {

        fun newInstance(): ExploreFragment {
            return ExploreFragment()
        }
    }


}