package com.ronaker.app.ui.manageProduct

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


class ManageProductFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentManageProductBinding
    private lateinit var productViewModel: ManageProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, com.ronaker.app.R.layout.fragment_manage_product, container, false)
        productViewModel = ViewModelProviders.of(this).get(ManageProductViewModel::class.java)


        productViewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })



        binding.toolbar.cancelClickListener = View.OnClickListener { (activity as DashboardActivity).backFragment() }

        productViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                binding.loading.showRetry()
            } else {
//                binding.loading.hideRetry()
            }
        })


        binding.viewModel = productViewModel




        productViewModel.loadProduct(this.arguments!!.getString(SUID_KEY))





        return binding.root
    }


    companion object {

        private var SUID_KEY = "suid"

        fun newInstance(suid: String?): ManageProductFragment {
            val bundle = Bundle()
            bundle.putString(SUID_KEY, suid)
            val fragment = ManageProductFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}
