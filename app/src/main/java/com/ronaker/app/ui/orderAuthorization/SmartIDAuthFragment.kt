package com.ronaker.app.ui.orderAuthorization

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
class SmartIDAuthFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderSmartidAuthBinding
    private val viewModel: OrderAuthorizationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_order_smartid_auth,
                container,
                false
            )

        binding.viewModel = viewModel

        return binding.root
    }


    companion object {

        fun newInstance(): SmartIDAuthFragment {
            return SmartIDAuthFragment()
        }
    }

    override fun onSelect() {
    }

}