package com.ronaker.app.ui.exploreProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarActivity

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



        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {

            try {
                val scrollY = binding.scrollView.scrollY

                if (scrollY <= binding.avatarImage.height/2 - binding.toolbar.bottom) {

                    binding.toolbar.isTransparent = true
                    binding.toolbar.isBottomLine = false
                    context?.let {
                        binding.statusBar.setBackgroundColor(
                            ContextCompat.getColor(it,R.color.transparent)
                        )
                    }

                } else {

                    binding.toolbar.isTransparent = false
                    binding.toolbar.isBottomLine = true

                    context?.let {    binding.statusBar.setBackgroundColor(ContextCompat.getColor(it,R.color.white)) }
                }

            } catch (ex: Exception ){

            }
        };



        productViewModel.checkout.observe(this, Observer { suid ->

            startActivityMakeScene(context?.let { CheckoutCalendarActivity.newInstance(it,productViewModel.product) })

        })



        return binding.root
    }




}
