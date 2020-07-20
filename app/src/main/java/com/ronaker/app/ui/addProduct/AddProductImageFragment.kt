package com.ronaker.app.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.ScreenCalculator
import com.ronaker.app.utils.view.IPagerFragment


class AddProductImageFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddImageBinding
    private lateinit var viewModel: AddProductViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_add_image, container, false)
        activity?.let {
            viewModel = ViewModelProvider(it).get(AddProductViewModel::class.java)
            binding.viewModel = viewModel
        }


        val screenMnager = ScreenCalculator(requireContext())


        val itemsize = 170
        val screensize = screenMnager.screenWidthDP.toInt()


        var count = screensize / itemsize

        if (count < 2)
            count = 2



        binding.recycler.layoutManager = GridLayoutManager(context, count)


        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)
//        binding.recycler.setOnTouchListener { _, _ -> true }

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