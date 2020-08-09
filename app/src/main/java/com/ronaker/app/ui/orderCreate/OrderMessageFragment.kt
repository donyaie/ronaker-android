package com.ronaker.app.ui.orderCreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment

class OrderMessageFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderMessageBinding
    private lateinit var viewModel: OrderCreateViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_message, container, false)
        activity?.let {
            viewModel = ViewModelProvider(it).get(OrderCreateViewModel::class.java)
            binding.viewModel = viewModel
        }



        return binding.root
    }


    companion object {

        fun newInstance(): OrderMessageFragment {
            return OrderMessageFragment()
        }
    }

    override fun onSelect() {
    }

}