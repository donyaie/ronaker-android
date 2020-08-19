package com.ronaker.app.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentInboxBinding
    private val viewModel: InboxViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false)



        binding.viewModel = viewModel


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loading.observe(this.viewLifecycleOwner, {loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })


        viewModel.errorMessage.observe(viewLifecycleOwner, {errorMessage ->
            if (errorMessage != null) {
                Alert.makeTextError(this, errorMessage)
//                binding.loading.showRetry()
            }
        })



    }


    companion object {

        fun newInstance(): InboxFragment {
            return InboxFragment()
        }
    }


}