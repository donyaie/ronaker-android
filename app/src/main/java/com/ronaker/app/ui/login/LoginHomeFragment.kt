package com.ronaker.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import kotlinx.android.synthetic.main.fragment_login_home.*

class LoginHomeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginHomeBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_login_home,container , false)


        activity?.let {
            viewModel = ViewModelProviders.of(it).get(LoginViewModel::class.java)
            binding.viewModel = viewModel
        }



        return binding.root
    }


    companion object {

        fun newInstance(): LoginHomeFragment {
            return LoginHomeFragment()
        }
    }
    override fun onSelect() {

    }
}