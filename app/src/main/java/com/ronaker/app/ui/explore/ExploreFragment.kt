package com.ronaker.app.ui.explore

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.search.SearchActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.ScreenCalculator
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener

class ExploreFragment : BaseFragment(), DashboardActivity.MainaAtivityListener {

    private lateinit var binding: com.ronaker.app.databinding.FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)
        viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)


        val screenMnager = ScreenCalculator(requireContext())


        val itemsize = 170
        val screensize = screenMnager.screenWidthDP.toInt()


        var count = screensize / itemsize

        if (count < 2)
            count = 2


        binding.viewModel = viewModel


        binding.categoryRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val mnager = GridLayoutManager(context, count)
        binding.recycler.layoutManager = mnager
        binding.loading.hideLoading()

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            binding.refreshLayout.isRefreshing = loading

        })
        viewModel.retry.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }

        })


        viewModel.scrollCategoryPosition.observe(viewLifecycleOwner, Observer { position ->
            binding.categoryRecycler.scrollToPosition(position)


        })





        viewModel.searchText.observe(viewLifecycleOwner, Observer { text ->


            if (text.isNullOrBlank()) {

                binding.searchText.text = getString(R.string.title_search_here)
                binding.backImage.visibility = View.GONE
                binding.searchImage.visibility = View.VISIBLE

            } else {
                binding.searchText.text = text
                binding.backImage.visibility = View.VISIBLE
                binding.searchImage.visibility = View.GONE
            }

        })


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }


        viewModel.searchValue.observe(viewLifecycleOwner, Observer { _ ->

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

//
//                val options = ActivityOptions
//                    .makeSceneTransitionAnimation(activity, binding.searchLayout, "search")

                val p1 =
                    androidx.core.util.Pair<View, String>(binding.searchLayout, "search")
                val p2 = androidx.core.util.Pair<View, String>(binding.cancelSearch, "searchCancel")
                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity as Activity, p1, p2)



                activity?.let {
                    startActivityForResult(
                        SearchActivity.newInstance(it),
                        SearchActivity.ResultCode,
                        options.toBundle()
                    )
                }

            } else {
                // Swap without transition
                activity?.let {
                    startActivityForResult(
                        SearchActivity.newInstance(it),
                        SearchActivity.ResultCode
                    )
                }
            }


        })

        viewModel.resetList.observe(viewLifecycleOwner, Observer {
            scrollListener.resetState()
        })


        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->

            Alert.makeTextError(this, errorMessage)

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



                if (!view.canScrollVertically(-1)) {

                    binding.header.cardElevation = 0f
                } else {
                    binding.header.cardElevation = 10f
                }

            }
        }

        binding.scrollView.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) { //code to fetch more data for endless scrolling

                    viewModel.loadMore()

                    AppDebug.log("load_more", "call")
                }


                if (!v.canScrollVertically(+1)) {

                    AppDebug.log("load_more", "call2")
                }

                if (!v.canScrollVertically(-1)) {

                    binding.header.cardElevation = 0f
                } else {
                    binding.header.cardElevation = 10f
                }


            }
        }


        binding.backImage.setOnClickListener {

            viewModel.clearSearch()
        }




        binding.categoryRecycler.addItemDecoration(object : ItemDecoration() {

            private val mEndOffset = context?.resources?.getDimensionPixelSize(R.dimen.margin_default)?:0



            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {


                super.getItemOffsets(outRect, view, parent, state)

                val dataSize = state.itemCount
                val position: Int = parent.getChildAdapterPosition(view)
                if (dataSize > 0 && position == 0) {
                    outRect.set(mEndOffset, 0, 0, 0)
                }  else {

                    outRect.set(0, 0, 0, 0)
                }
            }

        })


        if (activity is DashboardActivity)
            (activity as DashboardActivity).addFragmentListener(this, this)


        return binding.root
    }


    companion object {

        fun newInstance(): ExploreFragment {
            return ExploreFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == SearchActivity.ResultCode && resultCode == 0) {

            if (data != null) {
                val searchValue = data.getStringExtra(SearchActivity.Search_KEY)

                if (searchValue.isNullOrBlank())
                    viewModel.search("")
                else

                    viewModel.search(searchValue)
            }


        }

        super.onActivityResult(requestCode, resultCode, data)


    }



    override fun onDetach() {
        try {
//


            if (activity is DashboardActivity)
                (activity as DashboardActivity).removeFragmentListener(this)
        } catch (e: Exception) {

        }
        super.onDetach()
    }

    override fun onBackPressed(): Boolean {
        return viewModel.backPress()

    }


}