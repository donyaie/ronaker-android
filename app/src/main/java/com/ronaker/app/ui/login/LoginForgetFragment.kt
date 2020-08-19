package com.ronaker.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginForgetFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginForgetBinding
    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_forget, container, false)

            binding.viewModel = viewModel


        return binding.root
    }


    companion object {

        fun newInstance(): LoginForgetFragment {
            return LoginForgetFragment()
        }
    }

    override fun onSelect() {

    }


}