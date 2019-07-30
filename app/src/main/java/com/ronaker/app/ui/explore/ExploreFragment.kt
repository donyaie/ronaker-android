package com.ronaker.app.ui.explore

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
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
import com.ronaker.app.ui.search.SearchActivity
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent
import androidx.core.app.ActivityOptionsCompat



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
        viewModel.retry.observe(this, Observer { loading ->
            if (loading) binding.loading.showRetry() else binding.loading.hideRetry()
        })



        viewModel.searchValue.observe(this, Observer { value->

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

//
//                val options = ActivityOptions
//                    .makeSceneTransitionAnimation(activity, binding.searchLayout, "search")

                val p1 =  androidx.core.util.Pair<View,String>(binding.searchLayout, "search")
                val p2 =  androidx.core.util.Pair<View,String>(binding.cancelSearch, "searchCancel")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity as Activity, p1, p2)



                activity?.let { startActivityForResult( SearchActivity.newInstance(it),SearchActivity.ResultCode,options.toBundle()) }

            } else {
                // Swap without transition
                activity?.let { startActivityForResult( SearchActivity.newInstance(it),SearchActivity.ResultCode) }
            }






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

        binding.loading.oClickRetryListener = View.OnClickListener{

                viewModel.retry()


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


        binding.backImage.setOnClickListener {

            clearSearchValue()
        }


        binding.viewModel = viewModel


        return binding.root
    }


    companion object {

        fun newInstance(): ExploreFragment {
            return ExploreFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==SearchActivity.ResultCode && resultCode==0){

            if (data != null) {
               var searchValue= data.getStringExtra(SearchActivity.Search_KEY )

                setSearchValue(searchValue)
            }



        }

        super.onActivityResult(requestCode, resultCode, data)




    }



    fun setSearchValue(search:String){



        binding.searchText.text=search

        binding.backImage.visibility=View.VISIBLE
        binding.searchImage.visibility=View.GONE


        viewModel.search(search)


    }

    fun clearSearchValue(){


        binding.searchText.setText(R.string.title_search_here)

        binding.backImage.visibility=View.GONE
        binding.searchImage.visibility=View.VISIBLE
        viewModel.search(null)
    }

}