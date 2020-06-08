package com.ronaker.app.ui.profileAuthorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment

class SmartIDAuthFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentSmartidAuthBinding
    private lateinit var viewModel: ProfileAuthorizationViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_smartid_auth, container, false)
        activity?.let {
            viewModel = ViewModelProvider(it).get(ProfileAuthorizationViewModel::class.java)
            binding.viewModel = viewModel
        }



        return binding.root
    }



    companion object {

        fun newInstance(): SmartIDAuthFragment {
            return SmartIDAuthFragment()
        }
    }

    override fun onSelect() {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}