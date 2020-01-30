package com.ronaker.app.ui.orderMessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.extension.finishSafe


class OrderMessageFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderMessageBinding
    private lateinit var viewModel: OrderMessageViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_message, container, false)

        activity?.let {
            viewModel = ViewModelProvider(it).get(OrderMessageViewModel::class.java)
            binding.viewModel = viewModel
        }



        binding.toolbar.actionTextClickListener= View.OnClickListener {

        }



        binding.toolbar.cancelClickListener=View.OnClickListener {

            activity?.finishSafe()
        }


        return binding.root
    }



}
