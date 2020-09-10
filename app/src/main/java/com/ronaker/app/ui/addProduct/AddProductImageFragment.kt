package com.ronaker.app.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProductImageFragment : BaseFragment(), IPagerFragment {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddImageBinding

    private val viewModel: AddProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_add_image, container, false)
            binding.viewModel = viewModel




        val itemsize = requireContext().resources.getDimensionPixelSize(R.dimen.adapter_width)
        var screensize = binding.container.measuredWidth
        AppDebug.log("mnager", "screenSize : $screensize")
        var count = screensize / itemsize

        if (count < 2)
            count = 2

        var  mnager=  GridLayoutManager(context, count)
        binding.recycler.layoutManager = mnager

        val vtObserver = binding.root.viewTreeObserver
        vtObserver.addOnGlobalLayoutListener {

            if(screensize==0) {
                screensize = binding.container.measuredWidth

                AppDebug.log("mnager", "screenSize 2 : $screensize")
                count = screensize / itemsize

                if (count < 2)
                    count = 2
                mnager = GridLayoutManager(context, count)
                binding.recycler.layoutManager = mnager
            }

        }


        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)
//        binding.recycler.setOnTouchListener { _, _ -> true }

        return binding.root
    }


    companion object {

        fun newInstance(): AddProductImageFragment {
            return AddProductImageFragment()
        }
    }

    override fun onSelect() {
    }


}