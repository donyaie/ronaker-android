package com.ronaker.app.ui.orderAuthorization

import android.os.Bundle
import android.os.Debug
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Order
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.view.IPagerFragment
import io.reactivex.disposables.Disposable
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class SmartIDPersonalCodeFragment : BaseFragment(), IPagerFragment {


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
//        binding.view = this

//        binding.webView.loadUrl("file:///android_asset/contract.html");

        AppDebug.log("Show Order","order : "+getOrder().toString())
          getOrder()?.let {order->


              if(!isCanSign())
                  binding.nextButton.visibility=View.GONE

                val input: InputStream = requireContext().assets.open("contract.html")
                val size: Int = input.available()

                val buffer = ByteArray(size)
                input.read(buffer)
                input.close()

                val str = String(buffer)
                    .replace(
                        "[INCLUDE RENTER NAME]",
                        "${order.orderUser?.first_name} ${order.orderUser?.last_name}"
                    )
                    .replace(
                        "[INCLUDE LISTER NAME]",
                        "${order.productOwner?.first_name} ${order.productOwner?.last_name}"
                    )

                    .replace(
                        "[INCLUDE DATE]",
                        String.format(
                            "from %s to %s",
                            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(order.fromDate),
                            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(order.toDate))

                    )
//                    .replace(
//                        "[TRANSACTION NUMBER]",
//                       ""
//
//                    )

              AppDebug.log("Show Order","buffer size : "+buffer.size)

              AppDebug.log("Show Order", "html : \n$str")
//                .replace("[INCLUDE TRANSACTION NUMBER]", "new string")
//                .replace("[INCLUDE DATE]", "new string")

              binding.webView.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null)
//                binding.webView.loadData(str, "text/html", "utf-8")






            }?:run{
              requireActivity().finish()
          }




//        binding.pdfView.fromAsset("contract.pdf")
//            .enableSwipe(true) // allows to block changing pages using swipe
//            .swipeHorizontal(true)
//            .enableDoubletap(true)
//            .defaultPage(0)
//            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//            .password(null)
//            .scrollHandle(null)
//            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//            // spacing between pages in dp. To define spacing color, set view background
//            .spacing(0)
//            .pageFitPolicy(FitPolicy.WIDTH)
//            .load()


        return binding.root
    }





    companion object {

        fun newInstance(): SmartIDPersonalCodeFragment {
            return SmartIDPersonalCodeFragment()
        }
    }

    override fun onSelect() {

    }


    private fun isCanSign(): Boolean {
        if (requireActivity().intent.hasExtra(OrderAuthorizationActivity.CANSIGN_KEY)) {

            return requireActivity().intent.getBooleanExtra(OrderAuthorizationActivity.CANSIGN_KEY,true)

        }
        return true
    }


    private fun getOrder(): Order? {
        if (requireActivity().intent.hasExtra(OrderAuthorizationActivity.Order_KEY)) {

            return requireActivity().intent.getParcelableExtra<Order?>(OrderAuthorizationActivity.Order_KEY)

        }
        return null
    }


    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }


}