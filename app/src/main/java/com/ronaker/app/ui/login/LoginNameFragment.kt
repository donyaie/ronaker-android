package com.ronaker.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment

class LoginNameFragment : BaseFragment(){

    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginHomeBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_login_name,container , false)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    companion object {

        fun newInstance(): LoginNameFragment {
            return LoginNameFragment()
        }
    }

}