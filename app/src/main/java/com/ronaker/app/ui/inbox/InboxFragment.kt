package com.ronaker.app.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ronaker.app.utils.Alert
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment

class InboxFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentInboxBinding
    private lateinit var viewModel: InboxViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false)
        viewModel = ViewModelProvider(this).get(InboxViewModel::class.java)


        viewModel.loading.observe(this.viewLifecycleOwner, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })


        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Alert.makeTextError(this, errorMessage)
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