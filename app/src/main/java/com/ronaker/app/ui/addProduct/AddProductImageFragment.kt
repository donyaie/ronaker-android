package com.ronaker.app.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import android.view.MotionEvent



class AddProductImageFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddImageBinding
    private lateinit var viewModel: AddProductViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_add_image, container, false)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AddProductViewModel::class.java)
            binding.viewModel = viewModel
        }

        var mnager = GridLayoutManager(context, 2)

        binding.recycler.layoutManager = mnager
        binding.recycler.setOnTouchListener(View.OnTouchListener { v, event -> true })

        return binding.root
    }


    companion object {

        fun newInstance(): AddProductImageFragment {
            return AddProductImageFragment()
        }
    }

    override fun onSelect() {
    }


}