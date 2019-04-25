package com.ronaker.app.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity

class  DashboardActivity : BaseActivity(){

    private lateinit var binding: com.ronaker.app.databinding.ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        binding.viewModel = viewModel


    }


    companion object {
        fun newInstance(context: Context): Intent {
            return  Intent(context, DashboardActivity::class.java)
        }
    }


}