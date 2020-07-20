package com.ronaker.app.base

import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.General

abstract class BaseFragment : Fragment() {


    fun getAnalytics(): FirebaseAnalytics? {

        return if (requireActivity().applicationContext is General)
            (requireActivity().applicationContext as General).analytics
        else
            null
    }


}