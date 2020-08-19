package com.ronaker.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.BuildConfig
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSignInFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginSigninBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_signin, container, false)
        activity?.let {
            viewModel = ViewModelProvider(it).get(LoginViewModel::class.java)
            binding.viewModel = viewModel
        }


        if(BuildConfig.BUILD_TYPE.compareTo("robo")==0){
            binding.roboButton.visibility=View.VISIBLE
            binding.roboEmail.visibility=View.VISIBLE
            binding.roboPass.visibility=View.VISIBLE


        }else{

            binding.roboButton.visibility=View.GONE
            binding.roboEmail.visibility=View.GONE
            binding.roboPass.visibility=View.GONE
        }

        return binding.root
    }


    companion object {

        fun newInstance(): LoginSignInFragment {
            return LoginSignInFragment()
        }
    }

    override fun onSelect() {

    }


}