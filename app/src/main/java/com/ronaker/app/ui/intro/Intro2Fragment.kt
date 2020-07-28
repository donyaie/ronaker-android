package com.ronaker.app.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment


class Intro2Fragment : BaseFragment() {


    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginIntro2Binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_intro_2, container, false)


        return binding.root
    }


    companion object {

        fun newInstance(): Intro2Fragment {
            return Intro2Fragment()
        }
    }


}