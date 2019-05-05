package com.ronaker.app.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment

class AddProductPriceFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddPriceBinding
    private lateinit var viewModel: AddProductViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_product_add_price,container , false)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AddProductViewModel::class.java)
            binding.viewModel = viewModel
        }
        return binding.root
    }


    companion object {

        fun newInstance(): AddProductPriceFragment {
            return AddProductPriceFragment()
        }
    }

    override fun onSelect() {
    }


}