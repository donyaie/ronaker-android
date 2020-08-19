package com.ronaker.app.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductCategoryFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddCategoryBinding
    private val viewModel: AddProductViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_add_category,
            container,
            false
        )


        binding.viewModel = viewModel




        return binding.root
    }


    companion object {

        fun newInstance(): AddProductCategoryFragment {
            return AddProductCategoryFragment()
        }
    }

    override fun onSelect() {

    }
}