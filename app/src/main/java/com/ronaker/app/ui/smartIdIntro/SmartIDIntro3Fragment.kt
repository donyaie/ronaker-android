package com.ronaker.app.ui.smartIdIntro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.IntentManeger


class SmartIDIntro3Fragment : BaseFragment() {


    private lateinit var binding: com.ronaker.app.databinding.FragmentSmartIdIntro3Binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_smart_id_intro_3, container, false)

        binding.image.setOnClickListener {


            IntentManeger.openUrl(requireContext(),"https://play.google.com/store/apps/details?id=com.smart_id")
        }

        return binding.root
    }


    companion object {

        fun newInstance(): SmartIDIntro3Fragment {
            return SmartIDIntro3Fragment()
        }
    }


}