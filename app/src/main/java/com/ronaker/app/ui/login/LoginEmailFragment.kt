package com.ronaker.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import com.ronaker.app.utils.view.InputComponent
import io.reactivex.Observable

class LoginEmailFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginEmailBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_email, container, false)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(LoginViewModel::class.java)
            binding.viewModel = viewModel
        }

        viewModel.emailError.observe(this, Observer {
                errorMessage-> if (errorMessage!=null)binding.emailInput.showNotValidAlert()else binding.emailInput.hideAlert()
        })



        return binding.root
    }


    companion object {

        fun newInstance(): LoginEmailFragment {
            return LoginEmailFragment()
        }
    }

    override fun onSelect() {
    }


}