package com.ronaker.app.ui.exploreProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import android.view.ViewTreeObserver.OnScrollChangedListener
import kotlinx.android.synthetic.main.component_toolbar.view.*


class ExploreProductFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentExploreProductBinding
    private lateinit var productViewModel: ExploreProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, com.ronaker.app.R.layout.fragment_explore_product, container, false)
        productViewModel = ViewModelProviders.of(this).get(ExploreProductViewModel::class.java)


        productViewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })




        productViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                binding.loading.showRetry()
            } else {
//                binding.loading.hideRetry()
            }
        })


        binding.viewModel = productViewModel


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {


            val scrollY = binding.scrollView.scrollY

            if (scrollY <= binding.avatarImage.height - binding.toolbar.bottom) {

                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false
                binding.statusBar.setBackgroundColor(resources.getColor(R.color.transparent))

            } else {

                binding.toolbar.isTransparent = false
                binding.toolbar.isBottomLine = true

                binding.statusBar.setBackgroundColor(resources.getColor(R.color.white))
            }


        };


        productViewModel.loadProduct(this.arguments!!.getString(SUID_KEY))





        return binding.root
    }


    companion object {

        private var SUID_KEY = "suid"

        fun newInstance(suid: String?): ExploreProductFragment {
            val bundle = Bundle()
            bundle.putString(SUID_KEY, suid)
            val fragment = ExploreProductFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}
