package com.ronaker.app.ui.manageProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.addProduct.AddProductActivity
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener

class ManageProductListFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentManageProductListBinding

    private val viewModel: ManageProductListViewModel by viewModels()





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

        binding.viewModel = viewModel



        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.loading.hideLoading()




        val itemsize = requireContext().resources.getDimensionPixelSize(R.dimen.adapter_width)
        var screensize = binding.container.measuredWidth
        AppDebug.log("mnager", "screenSize : $screensize")
        var count = screensize / itemsize

        if (count < 2)
            count = 2
         val productAdapter=  ManageProductAdapter()
        var  mnager=  GridLayoutManager(context, count)
        binding.recycler.layoutManager = mnager
        binding.recycler.adapter=productAdapter

        val vtObserver = binding.root.viewTreeObserver
        vtObserver.addOnGlobalLayoutListener {

            if(screensize==0) {
                screensize = binding.container.measuredWidth

                AppDebug.log("mnager", "screenSize 2 : $screensize")
                count = screensize / itemsize

                if (count < 2)
                    count = 2
                mnager = GridLayoutManager(context, count)
                binding.recycler.layoutManager = mnager
            }

        }






//        binding.recycler.setOnTouchListener(View.OnTouchListener { v, event -> true })

        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)

        viewModel.loading.observe(viewLifecycleOwner, { loading ->
            binding.refreshLayout.isRefreshing = loading
        })
        viewModel.listView.observe(viewLifecycleOwner, {
           productAdapter.submitList(it.toList())
        })



        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }


        viewModel.retry.observe(viewLifecycleOwner, { loading ->
            loading?.let { binding.loading.showRetry(loading) }
                ?: run { binding.loading.hideRetry() }


        })

        viewModel.emptyView.observe(viewLifecycleOwner, { loading ->
            if (loading) {
                binding.emptyLayout.visibility = View.VISIBLE
            } else {
                binding.emptyLayout.visibility = View.GONE
            }
        })

        viewModel.addNewView.observe(viewLifecycleOwner, { loading ->
            if (loading) {

                binding.addNewProductButton.visibility = View.VISIBLE
                binding.addNewProductButton.isClickable = true
            } else {


                binding.addNewProductButton.visibility = View.INVISIBLE
                binding.addNewProductButton.isClickable = false
            }
        })




        binding.addNewProductButton.setOnClickListener {
            if(viewModel.profileIsComplete()) {

                activity?.startActivity(activity?.let { it1 -> AddProductActivity.newInstance(it1) })
            }else{

                activity?.startActivity(activity?.let { it1 -> ProfileCompleteActivity.newInstance(it1) })
            }
        }
        binding.addProductButton.setOnClickListener {

            if(viewModel.profileIsComplete()) {

                activity?.startActivity(activity?.let { it1 -> AddProductActivity.newInstance(it1) })
            }else{

                activity?.startActivity(activity?.let { it1 -> ProfileCompleteActivity.newInstance(it1) })
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null) {
                Alert.makeTextError(this, errorMessage)
            }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()


        }

        viewModel.resetList.observe(viewLifecycleOwner, {
            scrollListener.resetState()
        })


        scrollListener = object :
            EndlessRecyclerViewScrollListener(binding.recycler.layoutManager as GridLayoutManager) {
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