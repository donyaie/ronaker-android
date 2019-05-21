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

class ExploreProductFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentExploreProductBinding
    private lateinit var productViewModel: ExploreProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore_product, container, false)

        activity?.let {
            productViewModel = ViewModelProviders.of(it).get(ExploreProductViewModel::class.java)
            binding.viewModel = productViewModel
        }


        binding.toolbar.cancelClickListener = View.OnClickListener { activity ?.onBackPressed() }


        binding.viewModel = productViewModel


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {

            try {
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

            } catch (ex: Exception ){

            }
        };



        productViewModel.checkout.observe(this, Observer { suid ->

            Toast.makeText(context,suid,Toast.LENGTH_LONG).show()

        })



        return binding.root
    }




}
