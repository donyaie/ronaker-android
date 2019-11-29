package com.ronaker.app.ui.inbox

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

class InboxFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentInboxBinding
    private lateinit var viewModel: InboxViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false)
        viewModel = ViewModelProviders.of(this).get(InboxViewModel::class.java)


        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                binding.loading.showRetry()
            }
        })

        binding.viewModel = viewModel


        return binding.root
    }


    companion object {

        fun newInstance(): InboxFragment {
            return InboxFragment()
        }
    }


}