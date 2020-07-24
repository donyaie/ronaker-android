package com.ronaker.app.ui.orderAuthorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_order_smartid_personal_code.*

class SmartIDPersonalCodeFragment : BaseFragment(), IPagerFragment, OnLoadCompleteListener {


    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderSmartidPersonalCodeBinding
    private lateinit var viewModel: OrderAuthorizationViewModel

    var disposable: Disposable? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_smartid_personal_code,
            container,
            false
        )


        activity?.let {
            viewModel = ViewModelProvider(it).get(OrderAuthorizationViewModel::class.java)
            binding.viewModel = viewModel


        }
        binding.view = this

        binding.pdfView.fromAsset("contract.pdf")
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            .spacing(0)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()


        return binding.root
    }


    companion object {

        fun newInstance(): SmartIDPersonalCodeFragment {
            return SmartIDPersonalCodeFragment()
        }
    }

    override fun onSelect() {

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    override fun loadComplete(nbPages: Int) {

    }

}