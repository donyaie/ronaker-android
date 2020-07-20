package com.ronaker.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.language.LanguageDialog
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment

class LoginHomeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginHomeBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_home, container, false)


        activity?.let {
            viewModel = ViewModelProvider(it).get(LoginViewModel::class.java)
            binding.viewModel = viewModel
        }

        binding.languageChange.setOnClickListener {
            activity?.let { it1 -> LanguageDialog.showDialog(it1) }
        }

        AppDebug.log("capture", "LoginEmailFragment : CreateView")

        return binding.root
    }


    companion object {

        fun newInstance(): LoginHomeFragment {
            return LoginHomeFragment()
        }
    }

    override fun onSelect() {

        activity?.let { KeyboardManager.hideSoftKeyboard(it) }
    }
}