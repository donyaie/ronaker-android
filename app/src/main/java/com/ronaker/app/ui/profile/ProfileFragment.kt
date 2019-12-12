package com.ronaker.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.productSaved.ProductSavedActivity
import com.ronaker.app.ui.profileEdit.ProfileEditActivity
import com.ronaker.app.ui.profileSetting.ProfileSettingActivity
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.extension.startActivityMakeScene


class ProfileFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)


        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.logOutAction.observe(this, Observer {

            activity?.let {   it.startActivityMakeScene(DashboardActivity.newInstance(it) )}
        })



        binding.profileLayout.setOnClickListener {
            activity?.let {   it.startActivityMakeScene(ProfileEditActivity.newInstance(it) )}
        }



        binding.viewModel = viewModel


        binding.supportLayout.setOnClickListener {
            activity?.let { it1 -> IntentManeger.sendMail(it1,"support@ronaker.com")}
        }


        binding.faveLayout.setOnClickListener{


            activity?.let {   it.startActivityMakeScene(ProductSavedActivity.newInstance(it) )}

        }

        binding.settingLayout.setOnClickListener{

            activity?.let {   it.startActivityMakeScene(ProfileSettingActivity.newInstance(it) )}
        }



        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateUser()
    }


    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }




}